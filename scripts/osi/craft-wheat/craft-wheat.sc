Program CraftWheat;
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}
const SKILLET_TYPE = $97F;
const COOKING_GUMP_ID = $1CC;
const LAMA_ID = $6055443;//$6055443;
const NUM_GUMP_BUTTON = 1;
var FLOUR_SACK_TYPES: Array of Word = [$1045, $1039];
const RUNIC_ATLAS = $421AAB12;
const WITH_PACK_ANIMAL = False;
const VENDOR_FLOUR = $F031E;// $F045D;
const VENDOR_NAME = 'Sa';//'Lumtem';

const VENDOR_MAGINCIA = $5E593CE;

var VendorMaginciaPoint, BakershopPoint: TPoint;

function FindSkillet(ToolType: Word): Cardinal;
begin
  if FindType(ToolType, Backpack) = 0 then
    begin
      AddToSystemJournal('[Critical] Skillets not found!');
      Exit;
    end;
  Result := FindItem;
end;

procedure GetCookingGump(GumpSerial: Cardinal);
var
  i: Integer;
begin
  for i := 0 to GetGumpsCount-1 do
    if (GetGumpSerial(i) = GumpSerial) then Exit else CloseSimpleGump(i);

  UseObject(GumpSerial);
  if NOT WaitGumpMaxMS(3000, COOKING_GUMP_ID) then AddToSystemJournal('[Critical] No First cooking gump present!');  
end;

procedure CraftSackOfFlour();
const MAX_RETRY_COUNTER = 200;
var 
  currTool: Cardinal;
begin  
  if IsObjectExists(currTool) AND (GetParent(currTool) = Backpack) then
    GetCookingGump(currTool)
  else
    begin
      currTool := FindSkillet(SKILLET_TYPE);
      if currTool = 0 then Exit;  
      GetCookingGump(currTool);
    end;
  NumGumpButton(GetGumpsCount-1, NUM_GUMP_BUTTON);
  WaitGumpMaxMS(5000, COOKING_GUMP_ID);
  Wait(500);
end;

function PackAnimalIsFull(AnimalId: Cardinal): boolean;
var 
  loot: Array of Cardinal;
  k: Integer;
begin
  Result := False;
  if (StrContains(LowerCase(GetTooltip(AnimalId)), 'weight: 1513 stones')) then
    begin
      FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [AnimalId], false);
      GetFoundItemsFromStringList(loot);
      for k := 0 to High(loot) do
      if StrContains(LowerCase(GetTooltip(loot[k])), 'exceptional') then Continue
        else begin
          MoveItem(loot[k], FindFullQuantity - 1, Ground, GetX(Self)-1, GetY(Self), GetZ(Self));
          Wait(1500);
        end;
      if (StrContains(LowerCase(GetTooltip(AnimalId)), 'weight: 1513 stones')) then Result := True;
    end;  
end;

procedure Recall(RuneButton: Integer; SpellButton: Integer; SpellBookId: Cardinal; TargetPoint: TPoint);
begin
  While (Mana < 10) do Wait(1000);
  UseObject(SpellBookId);
  Wait(1500);
  NumGumpButton(GetGumpsCount-1, RuneButton);
  Wait(1500);
  NumGumpButton(GetGumpsCount-1, SpellButton);
  Wait(3500);

  if (Dist(GetX(Self), GetY(Self), TargetPoint.X, TargetPoint.Y) > 100) then
    begin 
      AddToSystemJournal('Recall failed, retry...');
      Recall(RuneButton, SpellButton, SpellBookId, TargetPoint);
    end;
end;

procedure SellToVendorInMagincia();
begin
  if Dist(GetX(Self), GetY(Self), VendorMaginciaPoint.X, VendorMaginciaPoint.Y) > 100 then
    Recall(50001, 5000, RUNIC_ATLAS, VendorMaginciaPoint);
  NewMoveXY(VendorMaginciaPoint.X, VendorMaginciaPoint.Y, true, 1, true);
  CloseAllGumps();

  UseObject(VENDOR_MAGINCIA);
  Wait(1500);

  While Weight > (MaxWeight * 0.8) do
    begin
      FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Backpack], false);      
      NumGumpRadiobutton(GetGumpsCount-1, -1024153, -1024153);
      NumGumpTextEntry(GetGumpsCount-1, 8, FindFullQuantity.toString());
      NumGumpButton(GetGumpsCount-1, 14);
      Wait(1500);
      NumGumpButton(GetGumpsCount-1, 15);
      Wait(1500);
    end;

  CloseAllGumps();
end;

procedure SellNonExceptionlSaks();
var
  loot: Array of Cardinal;
  k, i: Integer;
begin
  if Not (IsObjectExists(VENDOR_FLOUR)) then
    begin
      AddToSystemJournal('[Critical] VENDOR_FLOUR NOT FOUND!!!');
      while true do wait(1000);
    end;
  NewMoveXY(GetX(VENDOR_FLOUR), GetY(VENDOR_FLOUR), true, 1, true);
  FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Backpack], false);
  GetFoundItemsFromStringList(loot);
  for k := 0 to High(loot) do
  if NOT StrContains(LowerCase(GetTooltip(loot[k])), 'exceptional') then Continue
    else begin
      MoveItem(loot[k], 0, Ground, GetX(Self)+1, GetY(Self), GetZ(Self));
      Wait(1500);
    end;

  for i := 0 to High(FLOUR_SACK_TYPES) do
    AutoSell(FLOUR_SACK_TYPES[i], $FFFF, 9999);

  UoSay(VENDOR_NAME + ' sell');
  Wait(3000);

  for i := 0 to High(FLOUR_SACK_TYPES) do
    AutoSell(FLOUR_SACK_TYPES[i], $FFFF, 0);    
  
  FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Ground], false);  
  GetFoundItemsFromStringList(loot);
  for k := 0 to High(loot) do      
    if NOT StrContains(LowerCase(GetTooltip(loot[k])), 'exceptional') then Continue
      else begin
        NewMoveXY(GetX(loot[k]), GetY(loot[k]), true, 1, true);
        MoveItem(loot[k], 0, Backpack, 0,0,0);
        Wait(1500);
      end;
end;

var
  items: Array of Cardinal;
  i : Integer;
begin
  VendorMaginciaPoint.X := 3695;
  VendorMaginciaPoint.Y := 2164;
  // Ter Mur
  BakershopPoint.X := 805;
  BakershopPoint.Y := 3489;
  While Not Dead And Connected do
    begin      
      if (FindType($1EBD, Backpack) = 0) then Break;
      if WITH_PACK_ANIMAL And PackAnimalIsFull(LAMA_ID) then Break;
      if (Dist(GetX(Self), GetY(Self), BakershopPoint.X, BakershopPoint.Y) > 100) then Recall(50000, 5000, RUNIC_ATLAS, BakershopPoint);
      NewMoveXY(BakershopPoint.X, BakershopPoint.Y, true, 1, true);
      CraftSackOfFlour();
      FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Backpack], false);
      GetFoundItemsFromStringList(items);

      if WITH_PACK_ANIMAL then
        for i:= 0 to High(items) do
          begin
            MoveItem(items[i], 0, LAMA_ID, 0,0,0);
            Wait(1000);
          end;

      if Weight > (MaxWeight * 0.9) then
        begin
          SellNonExceptionlSaks();
          if Weight > (MaxWeight * 0.85) then SellToVendorInMagincia();
        end;
    end;
  Beep;
end.