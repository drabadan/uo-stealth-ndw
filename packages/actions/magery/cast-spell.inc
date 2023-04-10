procedure Executor(var state: TState; const command: TCommand);
begin
    AddToSystemJournal('Casting Spell: ' + command.Title);
end;

function Condition(const state: TState): boolean;
begin
    Result := True;
end;

// constructor
function CastSpellAction(const SpellName: String; const ManaRequired: Word): TCommand;
begin
    Result.Title := 'Cast Spell ' + SpellName;
    Result.Condition := @Condition;
    Result.Executor := @Executor;
end;