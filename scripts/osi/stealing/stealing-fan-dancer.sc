program StealFanDancer;

{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}

const
  STEALING_MSG_END = 'successfully steal the';
  STEALING_MSG_RETRY = '';
  CORPSE_TYPE = $2006;

  function Steal(targetID: cardinal): boolean;
  begin
    if Not IsObjectExists(targetID) then 
    begin
      Result := True;
      Ignore(targetID);
      Exit;
    end;
    
    NewMoveXY(GetX(targetID), GetY(targetID), True, 1, True);
    if (GetDistance(targetID) <= 1) then
      begin
        WaitTargetObject(targetID);
        UseSkill('Stealing');
        if (WaitJournalLine(Now, STEALING_MSG_END, 3000)) then
        begin
          Result := True;
          Ignore(targetID);
          Exit;
        end;
      end;    
    Result := False;
  end;

  procedure Heal(targetID: cardinal);
  begin
    if GetHP(targetID) < (GetMaxHp(targetID) * 0.9) then
    begin
      WaitTargetObject(targetID);
      Cast('Heal');
      Wait(1000);
    end;
  end;

var
  LOOT_TYPES: array of word = [$EED];
  startPoint: TPoint;
  availableCorpses, lootItems: array of cardinal;
  i, k, waitDelta: integer;
begin
  FindDistance := 10;
  IgnoreReset;

  startPoint.X := GetX(Self);
  startPoint.Y := GetY(Self);

  while not Dead do
  begin
    while FindType($F7, Ground) > 0 do
      while not Steal(FindItem) do
      begin
        Wait(100);
        Heal(Self);
      end;

    NewMoveXY(startPoint.X, startPoint.Y, True, 1, True);
    waitDelta := 6000;
    FindType(CORPSE_TYPE, Ground);
    GetFoundItemsFromStringList(availableCorpses);
    for i := 0 to High(availableCorpses) do
    begin
      NewMoveXY(GetX(availableCorpses[i]), GetY(availableCorpses[i]), True, 1, True);
      UseObject(availableCorpses[i]);
      if WaitJournalLine(Now, 'did not earn the right', 2000) then
        Continue;
      // Wait(1000);
      waitDelta := waitDelta - 1000;
      // FindTypesArrayEx([$FFFF], [$FFFF], [availableCorpses[i]], false);
      FindType($FFFF, availableCorpses[i]);
      GetFoundItemsFromStringList(lootItems);
      for k := 0 to High(lootItems) do
        if LOOT_TYPES.Contains(GetType(lootItems[k])) or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'legendary artifact') or
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'major artifact') or 
          StrContains(LowerCase(GetTooltip(lootItems[k])), 'greater artifact') then
        begin
          MoveItem(lootItems[k], 0, Backpack, 0, 0, 0);
          Wait(1000);
          waitDelta := waitDelta - 1000;
        end;
      Ignore(availableCorpses[i]);
    end;
    if waitDelta > 0 then
      Wait(waitDelta);
  end;
  Wait(150);
end.
