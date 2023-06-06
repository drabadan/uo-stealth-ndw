program CovetousHarpiesFarming;

{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}

var
  MONSTER_TYPES: array of word = [$1E];
  LOOT_TYPES: array of word = [
    // Gold
    $EED,
    // Stones
    $F0F, $F13, $F10, $F11, $F15, $F26, $F18, $F25, $F16,
    $1BD1
   ];
const
  CONFIDENCE_ATTRIBUTE_ID = $00000445;
const
  CORPSE_TYPE = $2006;

procedure InitCovetousEntranceTraps();
begin
SetBadLocation(5459, 1856);
SetBadLocation(5463, 1855);
SetBadLocation(5463, 1852);
SetBadLocation(5467, 1852);
SetBadLocation(5471, 1857);
SetBadLocation(5471, 1854);
SetBadLocation(5474, 1857);
SetBadLocation(5474, 1852);
SetBadLocation(5477, 1854);
SetBadLocation(5479, 1856);
end;

  procedure CheckAndUseConfidence();
  var
    buffObj: TBuffBarInfo;
    i: integer;
    found: boolean;
  begin
    buffObj := GetBuffBarInfo();
    for i := 0 to buffObj.Count - 1 do
      if buffObj.Buffs[i].Attribute_ID = CONFIDENCE_ATTRIBUTE_ID then
        found := True;

    if not found then
    begin
      if Mana < 10 then
        AddToSystemJournal('Low mana to cast confidence!');
      Cast('Confidence');
    end;
  end;


  procedure DoWarChecks();
  begin
    if HP < (MaxHP * 0.6) then
      AddToSystemJournal('Low HP');
    CheckAndUseConfidence();
  end;

  procedure CutTheCorpse(Corpse: Cardinal);
  begin
    WaitTargetObject(Corpse);
    UseObject(ObjAtLayer(LHandLayer));
    Wait(1000);
  end;

  procedure LootAllCorpses();
  var
    availableCorpses, lootItems: array of cardinal;
    i, k: integer;
  begin
    FindType(CORPSE_TYPE, Ground);
    GetFoundItemsFromStringList(availableCorpses);
    for i := 0 to High(availableCorpses) do
    begin
      NewMoveXY(GetX(availableCorpses[i]), GetY(availableCorpses[i]), True, 1, True);
      CutTheCorpse(availableCorpses[i]);
      UseObject(availableCorpses[i]);
      Wait(1500);
      // if WaitJournalLine(Now, 'did not earn the right', 2000) then
      //   Continue;
      FindType($FFFF, availableCorpses[i]);
      GetFoundItemsFromStringList(lootItems);
      AddToSystemJournal('Found items in corpse count: ', High(lootItems));
      for k := 0 to High(lootItems) do
        if LOOT_TYPES.Contains(GetType(lootItems[k])) or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'minor magic') or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'legendary artifact') or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'major artifact') or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'greater artifact') then
        begin
          MoveItem(lootItems[k], 0, Backpack, 0, 0, 0);
          Wait(1000);          
        end;
      Ignore(availableCorpses[i]);
    end;
  end;

  procedure KillCreature(Creature: cardinal);
  begin
    NewMoveXY(GetX(Creature), GetY(Creature), True, 1, True);
    WaitTargetObject(Creature);
    UseVirtue('Honor');
    Wait(2000);
    while not Dead and IsObjectExists(Creature) do
    begin
      if not NewMoveXY(GetX(Creature), GetY(Creature), True, 1, True) then
        AddToSystemJournal('Creature not reachable!');      
      Attack(Creature);
      DoWarChecks();
      Wait(1000);
    end;
  end;

begin
  FindDistance := 20;
  FindVertical := 255;
  InitCovetousEntranceTraps();
  while not Dead do
  begin
    NewMoveXY(5471, 1875, true, 0, true);
    AddToSystemJournal(FindType($001E, Ground));
    AddToSystemJournal(FindTypesArrayEx(MONSTER_TYPES, [$ffff], [Ground], False));
    if FindItem > 0 then
      KillCreature(FindItem)
    else AddToSystemJournal('No creatures found');
    LootAllCorpses();
    Wait(1000);
  end;
end.
