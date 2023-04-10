function RestockRegsCondition(const state: TState): boolean;
var 
  i, tq: Integer;
begin
  tq := 0;
  for i := REGS_CONST.Low to REGS_CONST.High do
    begin
      FindType(REGS_CONST[i], Backpack);
      tq := tq + FindFullQuantity;
    end;

  Result := tq >= (REGS_CONST.Length * state.MinPerRegQuantity);
end;

procedure RestockRegsExecutor(var state: TState; const command: TCommand);
var 
  i, d: Integer;
begin
  d := 0;
  for i := REGS_CONST.Low to REGS_CONST.High do
    begin
      FindType(REGS_CONST[i], Backpack);
      d := state.LoadRegsQuantity - FindFullQuantity;
      if NOT (MoveItem(FindType(REGS_CONST[i], Ground), d, Backpack, 0,0,0)) then
        AddToSystemJournal('[Critical] Cannot load Regs from floor: ', REGS_CONST[i]);
      Wait(MOVE_ITEM_DELAY_MS);
    end;
end;

function RestockRegs(): TCommand;
begin
  Result.Title := 'Restock Regs';
  Result.Condition := @RestockRegsCondition;
  Result.Executor := @RestockRegsExecutor;
  Result.Priority := HighPrio;
end;
