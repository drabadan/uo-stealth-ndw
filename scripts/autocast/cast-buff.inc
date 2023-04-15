function CastBuffCondition(const state: TState): boolean;
begin
  Result := state.BuffTargets.Length > 0;
end;

procedure CastBuffExecutor(var state: TState; const command: TCommand);
var 
  target: TBuffTarget;
  buffs: array of Char;
  i, k : Integer;
  spell: TSpell;
  splittedString: array of String;
begin
  target := state.BuffTargets.Pop();
  buffs := StrSplit(target.Phrase, '');
  for i := buffs.Low to buffs.High do 
    begin
      splittedString := StrSplit(SPELLS_MATCH[StrToInt(buffs[i])], ',');
      
      for k := splittedString.Low to splittedString.High do
        begin
          spell := FindSpellByName(splittedString[k]);
          if spell.SpellName <> '' then
            begin
              AddToSystemJournal('Casting: ' + spell.SpellName);
              // CastSpell(spell); 
            end;
        end;
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