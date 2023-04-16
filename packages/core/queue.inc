const DELAY_BETWEEN_RUNS_MS = 1000;

type
  TPriority = (HighPrio, MediumPrio, LowPrio);
  TCommand = record
    Title: string;
    Priority: TPriority;
    IsAssigned: boolean;
    Condition: Function(const state: TState): boolean;
    Executor: Procedure(var state: TState; const command: TCommand);
  end;
  TCommandArray = array of TCommand;

  TQueue = record
    CancellationToken: boolean;
    LowPrio:  TCommandArray;
    MediumPrio: TCommandArray;
    HighPrio: TCommandArray;
  end;

var
  gQueue: TQueue;
  gState: TState;

  procedure ReportLengths();
  begin
    AddToSystemJournal('High length: ', gQueue.HighPrio.Length);
    AddToSystemJournal('Medium length: ', gQueue.MediumPrio.Length);
    AddToSystemJournal('Low length: ', gQueue.LowPrio.Length);
  end;

  procedure CancelRunning();
  begin
    gQueue.CancellationToken := True;
    AddToSystemJournal('Cancel queue');
  end;

  procedure RegisterActions(Actions: TCommandArray);
  var
    i: integer;
    arr: TCommandArray;
  begin
    if (Actions.Length > 0) then
      for i := Actions.Low to Actions.High do
      begin
        if (Actions[i].Priority = LowPrio) then
        begin
          gQueue.LowPrio.Push(Actions[i]);
        end
        else if (Actions[i].Priority = MediumPrio) then
        begin
          gQueue.MediumPrio.Push(Actions[i]);
        end
        else if (Actions[i].Priority = HighPrio) then
        begin
          gQueue.HighPrio.Push(Actions[i]);
        end;
      end;
  end;

  function PopAction(arr: TCommandArray): TCommand;
  begin
    Result := arr.Pop();
    Result.IsAssigned := True;
  end;

  procedure ExecCommand(command: TCommand; state: TState);
  begin
    if command.Condition(state) then 
      begin        
        command.Executor(state, command);
        Wait(DELAY_BETWEEN_RUNS_MS);
    end;
  end;

  procedure Run(var state: TState);
  var i: Integer;
  begin
    for i := gQueue.HighPrio.Low to gQueue.HighPrio.High do
      ExecCommand(gQueue.HighPrio[i], state);
    for i := gQueue.MediumPrio.Low to gQueue.MediumPrio.High do
      ExecCommand(gQueue.MediumPrio[i], state);
    for i := gQueue.LowPrio.Low to gQueue.LowPrio.High do
      ExecCommand(gQueue.LowPrio[i], state);

    Wait(DELAY_BETWEEN_RUNS_MS);
  end;

  procedure RunUntilCancel(var state: TState);
  begin
    while not gQueue.CancellationToken do
      Run(state);
  end;