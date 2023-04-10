function RegsAvailable(regs: array of Word): boolean;
var i, tq: Integer;
begin
  tq := 0;
  for i := regs.Low to regs.High do
    begin
      FindType(regs[i], Backpack);
      tq := tq + FindFullQuantity;
    end;

  // It's always one reg per cast so we need as many regs as there is different required
  Result := tq >= regs.Length;
end;

function CanCast(Spell: TSpell): boolean;
begin
  Result := (Spell.ManaRequired <= Mana) AND RegsAvailable(Spell.Regs);
end;

procedure CastSpell(Spell: TSpell; TargetID: Cardinal);
begin
  if NOT CanCast(Spell) then
    begin 
      AddToSystemJournal('[Critical] Cannot cast Spell ', Spell.SpellName);
      Exit;
    end;  
  if (Spell.Targeted) then
    WaitTargetObject(TargetID);
  Cast(Spell.SpellName);
  WaitJournalLine(Now, 'fizzles', 3000);  
 end;