function CastBuffCondition(const state: TState): boolean;
begin
  Result := state.BuffTargets.Length > 0;
end;

procedure CastBuffExecutor(var state: TState; const command: TCommand);
var 
  target: TBuffTarget;
  buffs: array of Char;
  i : Integer;
begin
  target := state.BuffTargets.Pop();
  buffs := StrSplit(target.Phrase, '');
  for i := buffs.Low to buffs.High do 
    begin
      AddToSystemJournal(buffs[i]);
      AddToSystemJournal(SPELLS_MATCH[StrToInt(buffs[i])]);
    end;

  // CancelRunning();
end;

function CastBuffsAction(): TCommand;
begin
  Result.Title := 'Cast buff';
  Result.Condition := @CastBuffCondition;
  Result.Executor := @CastBuffExecutor;
  Result.Priority := HighPrio;
end;