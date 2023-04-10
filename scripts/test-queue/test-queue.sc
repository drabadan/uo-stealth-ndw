Program TestQueue;

{$I 'state.type.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
 
var
  actions: TCommandArray;
  i: integer;

  procedure Executor(var state: TState; const command: TCommand);
  begin
    AddToSystemJournal('Current command ', command.Title);
    state.RunsCount := state.RunsCount + 1;
    AddToSystemJournal('Executor: ', state.RunsCount);
    gState := state;
  end;

  function Condition(const state: TState): boolean;
  begin
    Result := True;
  end;

procedure Test();
begin
  gState.RunsCount := 0;
  gQueue.CancellationToken := False;
  SetLength(actions, 3);
  actions[0].Title := 'First command';
  actions[0].Condition := @Condition;
  actions[0].Executor := @Executor;
  actions[0].Priority := LowPrio;

  actions[1].Title := 'Second command';
  actions[1].Condition := @Condition;
  actions[1].Executor := @Executor;
  actions[1].Priority := MediumPrio;

  actions[2].Title := 'Third command';
  actions[2].Condition := @Condition;
  actions[2].Executor := @Executor;
  actions[2].Priority := HighPrio;

  RegisterActions(actions);
  Run(gState);
  Assert(gQueue.HighPrio.Length = 0);
  Run(gState);
  Assert(gQueue.MediumPrio.Length = 0);
  Run(gState);
  Assert(gQueue.LowPrio.Length = 0);
  AddToSystemJournal(gState.RunsCount);
  Assert(gState.RunsCount = 3);
end;

begin
Test;
end.