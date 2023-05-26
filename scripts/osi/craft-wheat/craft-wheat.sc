Program CraftWheat;
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}
const SKILLET_TYPE = $97F;
const COOKING_GUMP_ID = $1CC;
const LAMA_ID = $5236D67;
const NUM_GUMP_BUTTON = 1;
var FLOUR_SACK_TYPES: Array of Word = [$1045, $1039];

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

procedure CookFishSteakExecutor();
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

var
  items: Array of Cardinal;
  i : Integer;
begin
  While Not Dead And Connected do
    begin
      if (FindType($1EBD, Backpack) = 0) then Break;
      if (StrContains(LowerCase(GetTooltip(LAMA_ID)), 'weight: 1500 stones')) then Break;
      CookFishSteakExecutor();
      FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Backpack], false);
      GetFoundItemsFromStringList(items);
      for i:= 0 to High(items) do
        begin
          MoveItem(items[i], 0, LAMA_ID, 0,0,0);
          Wait(1000);
        end;
    end;
end.