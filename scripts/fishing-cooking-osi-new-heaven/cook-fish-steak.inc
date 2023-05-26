function CookFishSteakCondition(const state: TState): boolean;
begin
  FindType(RAW_FISH_STEAK_TYPE, Backpack);
  Result := FindFullQuantity >= state.CookingSteaksQuantityFlag;
end;

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

procedure CookFishSteakExecutor(var state: TState; const command: TCommand);
const MAX_RETRY_COUNTER = 200;
var 
  counter: Integer;
  currTool: Cardinal;
begin
  AddToSystemJournal('[INFO] Going cooking!');
  NewMoveXY(state.ForgePoint.X, state.ForgePoint.Y, true, 1, true);
  FindType(RAW_FISH_STEAK_TYPE, Backpack);
  MoveItem(FindItem, FindFullQuantity - 1, Ground, GetX(Self)-1, GetY(Self), GetZ(Self));
  Wait(1000);
  counter := 0;
  While (FindType(RAW_FISH_STEAK_TYPE, Ground) > 0) do
    begin
      Inc(counter);
      if counter > MAX_RETRY_COUNTER then AddToSystemJournal('[Warning] Max retries counter: ', counter,' over limit!');
      if FindType(RAW_FISH_STEAK_TYPE, Backpack) = 0 then
        begin
          MoveItem(FindType(RAW_FISH_STEAK_TYPE, Ground), 1, Backpack, 0,0,0);
          Wait(1000);
        end;
      
      if IsObjectExists(currTool) AND (GetParent(currTool) = Backpack) then
        GetCookingGump(currTool)
      else
        begin
          currTool := FindSkillet(SKILLET_TYPE);
          if currTool = 0 then Break;  
          GetCookingGump(currTool);
        end;
      NumGumpButton(GetGumpsCount-1, COOKING_GUMP_FISH_STEAK);
      WaitGumpMaxMS(5000, COOKING_GUMP_ID);
      Wait(500);
    end;

  CloseAllGumps();
end;

function CookFishSteakAction(): TCommand;
begin
  Result.Title := 'Cook fish steak';
  Result.Condition := @CookFishSteakCondition;
  Result.Executor := @CookFishSteakExecutor;
  Result.Priority := MediumPrio;
end;