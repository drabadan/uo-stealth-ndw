function HideCondition(const state: TState): boolean;
begin
  Result := NOT Hidden;
end;

procedure HideExecutor(var state: TState; const command: TCommand);
begin
  while NOT Hidden do 
    begin
      UseSkill('Hiding');
      if NOT WaitJournalLine(Now, HIDING_JOURNAL_MSG, 10000) then
        AddToSystemJournal('[Critical] Could not hide or get message!');
    end;
end;

function HideAction(): TCommand;
begin
  Result.Title := 'Hide';
  Result.Condition := @HideCondition;
  Result.Executor := @HideExecutor;
  Result.Priority := HighPrio;
end;