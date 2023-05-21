function CookFishSteakCondition(const state: TState): boolean;
begin
  FindType(RAW_FISH_STEAK_TYPE, Backpack);
  Result := FindFullQuantity >= state.CookingSteaksQuantityFlag;
end;

procedure GetCookingGump();
var
  i: Integer;
begin
  for i := 0 to GetGumpsCount-1 do
    if (GetGumpID(i) = COOKING_GUMP_ID) then Exit;
      
  if FindType(SKILLET_TYPE, Backpack) = 0 then
    begin
      AddToSystemJournal('[Critical] Skillets not found!');
      Exit;
    end;

  UseObject(FindItem);
  if NOT WaitGumpMaxMS(3000, COOKING_GUMP_ID) then AddToSystemJournal('[Critical] No First cooking gump present!');  
end;

procedure CookFishSteakExecutor(var state: TState; const command: TCommand);
begin
  AddToSystemJournal('[INFO] Going cooking!');
  NewMoveXY(state.ForgePoint.X, state.ForgePoint.Y, true, 1, true);
  FindType(RAW_FISH_STEAK_TYPE, Backpack);
  MoveItem(FindItem, FindFullQuantity - 1, Ground, GetX(Self)-1, GetY(Self), GetZ(Self));
  Wait(1000);
  While FindType(RAW_FISH_STEAK_TYPE, Ground) > 0 do
    begin
      if FindType(RAW_FISH_STEAK_TYPE, Backpack) = 0 then
        begin
          MoveItem(FindType(RAW_FISH_STEAK_TYPE, Ground), 1, Backpack, 0,0,0);
          Wait(1000);
        end;        
      GetCookingGump();
      NumGumpButton(GetGumpsCount-1, COOKING_GUMP_FISH_STEAK);
      WaitGumpMaxMS(5000, COOKING_GUMP_ID);
    end;
end;

function CookFishSteakAction(): TCommand;
begin
  Result.Title := 'Cook fish steak';
  Result.Condition := @CookFishSteakCondition;
  Result.Executor := @CookFishSteakExecutor;
  Result.Priority := HighPrio;
end;