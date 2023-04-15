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
var
  stime: TDateTime;
  varMana: Word;
begin
  if NOT CanCast(Spell) then
    begin 
      AddToSystemJournal('[Critical] Cannot cast Spell ', Spell.SpellName);
      Exit;
    end;  
  varMana := Mana;
  stime := Now;
  if (Spell.Targeted) then
    WaitTargetObject(TargetID);
  Cast(Spell.SpellName);
  repeat  
    InJournalBetweenTimes('fizzles', stime, Now);
    wait(100);
  until (LineCount > 0) or (varMana > Mana) or (Now > stime + (0.2 / 1440)) or Dead;
 end;