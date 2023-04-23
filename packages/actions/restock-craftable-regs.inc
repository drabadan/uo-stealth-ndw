function RestockCraftableRegsCondition(const state: TState): boolean;
var 
  i, tq: Integer;
begin
  tq := 0;
  for i := state.CurrentCraftable.Reagents.Low to state.CurrentCraftable.Reagents.High do
    begin
      FindType(state.CurrentCraftable.Reagents[i].Graphic, Backpack);
      if (FindFullQuantity < state.CurrentCraftable.Reagents[i].Quantity) then
        Exit(True);
    end;

  Result := False;
end;

procedure RestockCraftableRegsExecutor(var state: TState; const command: TCommand);
var 
  i, d: Integer;
begin
  d := 0;
  for i := state.CurrentCraftable.Reagents.Low to state.CurrentCraftable.Reagents.High do
    begin
      FindType(state.CurrentCraftable.Reagents[i].Graphic, Backpack);      
      d := state.LoadRegsQuantity - FindFullQuantity;   
      if d <= 0 then
        Continue;
      if NOT (MoveItem(FindType(state.CurrentCraftable.Reagents[i].Graphic, Ground), d, Backpack, 0,0,0)) then
        AddToSystemJournal('[Critical] Cannot load Regs from floor: ' + state.CurrentCraftable.Reagents[i].Title);
      Wait(MOVE_ITEM_DELAY_MS);
    end;
end;

function RestockCraftableRegs(): TCommand;
begin
  Result.Title := 'Restock Craftable Regs';
  Result.Condition := @RestockCraftableRegsCondition;
  Result.Executor := @RestockCraftableRegsExecutor;
  Result.Priority := HighPrio;
end;
