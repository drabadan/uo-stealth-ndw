function ScanFieldCondition(const state: TState): boolean;
begin
  Result := True;
end;

procedure ScanFieldExecutor(var state: TState; const command: TCommand);
begin  
  if NOT NewMoveXY(state.Fields[state.CurrentFieldIndex].CenterPoint.X, state.Fields[state.CurrentFieldIndex].CenterPoint.Y, true, 1, state.Run) then
    AddToSystemJournal('[Critical] field index: ', state.CurrentFieldIndex, ' at: X: ', state.Fields[state.CurrentFieldIndex].CenterPoint.X, ' Y: ', state.Fields[state.CurrentFieldIndex].CenterPoint.Y, ' not reachable!');
  FindTypesArrayEx(state.GatherTypes, [$FFFF], [Ground], false);  
  if FindCount = 0 then gState.CurrentFieldIndex := gState.CurrentFieldIndex + 1;  
  if gState.CurrentFieldIndex = Length(state.Fields) then gState.CurrentFieldIndex := 0;
end;

function ScanFieldAction(): TCommand;
begin
  Result.Title := 'Scan field for any ready to gather materials';
  Result.Condition := @ScanFieldCondition;
  Result.Executor := @ScanFieldExecutor;
  Result.Priority := MediumPrio;
end;