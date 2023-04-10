function CheckManaCondition(const state: TState): boolean;
begin
  Result := Mana <= state.CriticalManaValue;
end;

procedure CheckManaExecutor(var state: TState; const command: TCommand);
var 
  medTimer: TDateTime;
  ma, mb: Integer;  
begin
  while Mana <> Int do
    begin
      medTimer := now;
      UseSkill('Meditation');
      repeat
        ma := InJournalBetweenTimes(MEDITATION_FAIL_MSG, medTimer, Now);
        mb := InJournalBetweenTimes(MEDITATION_FINISH_MSG, medTimer, Now);
        wait(100);        
      until (ma >= 0) or (mb >= 0) or (Now > (medTimer + (0.168 / 1440)));
      if (ma >= 0) then Wait(10500);
      Wait(100);
    end;
end;

function CheckManaAction(): TCommand;
begin
  Result.Title := 'Check Mana';
  Result.Condition := @CheckManaCondition;
  Result.Executor := @CheckManaExecutor;
  Result.Priority := HighPrio;
end;