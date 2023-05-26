function MillCondition(const state: TState): boolean;
begin
  FindTypesArrayEx(state.LootTypes, [$FFFF], [Backpack], false);
  Result := FindFullQuantity >= state.UnloadQuantity;
end;

procedure MillExecutor(var state: TState; const command: TCommand);
var
  items: Array of Cardinal;
  i: Integer;
  millId: Cardinal;
begin
  NewMoveXY(state.FlourMillPoint.X, state.FlourMillPoint.Y, true, 0, false);
  FindTypesArrayEx(state.LootTypes, [$FFFF], [Backpack], false);
  GetFoundItemsFromStringList(items);

  if Length(items) > 0 then
    for i:= 0 to High(items) do
      begin
        MoveItem(items[i], 0, state.UnloadChestId, 0,0,0);
        Wait(1000);
      end;

  millId := FindType(FLOUR_MILL_TYPE, Ground);
  if millId = 0 then AddToSystemJournal('[Critical] Flour mill not found!');

  while FindTypesArrayEx(state.LootTypes, [$FFFF], [state.UnloadChestId], false) > 0 do
    begin
      WaitTargetObject(millId);
      UseObject(FindItem);
      Wait(1000);
      UseObject(millId);
      if WaitJournalLine(Now, 'need more wheat to make', 2000) then 
        begin
          Wait(1000);
          Continue;
        end;

      for i := 0 to 10 do
        begin
          Wait(1000);
          if FindTypesArrayEx(FLOUR_SACK_TYPES, [$FFFF], [Ground], false) > 0 then
            begin
              MoveItem(FindItem, 0, state.UnloadChestId, 0,0,0);
              Wait(1000);
              Break;
            end;
        end;      
    end;
end;

function MillAction(): TCommand;
begin
  Result.Title := 'Cook fish steak';
  Result.Condition := @MillCondition;
  Result.Executor := @MillExecutor;
  Result.Priority := HighPrio;
end;