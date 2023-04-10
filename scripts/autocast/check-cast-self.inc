function CheckCastSelfCondition(const state: TState): boolean;
begin
  Result := Int < state.MinIntValue;
end;

procedure CheckCastSelfExecutor(var state: TState; const command: TCommand);
begin
  CastSpell(DispelSpell, Self);
  CastSpell(BlessSpell, Self);
  CastSpell(CunningSpell, Self);
  CastSpell(StrengthSpell, Self);
  CastSpell(AgilitySpell, Self);
end;

function CheckCastSelfAction(): TCommand;
begin
  Result.Title := 'Check And Cast Self';
  Result.Condition := @CheckCastSelfCondition;
  Result.Executor := @CheckCastSelfExecutor;
  Result.Priority := HighPrio;
end;