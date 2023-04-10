type
  TSpell = record
    SpellName: String;
    Regs: array of Word;
    ManaRequired: Word;
    Harmfull: boolean;
    Buff: boolean;
    Targeted: boolean;
end;

function DispelSpell: TSpell;
begin
  Result.SpellName:= 'Dispel';
  Result.Regs:= [GA, MR, SA];
  Result.ManaRequired := 20;
  Result.Harmfull := False;
  Result.Buff:= False;
  Result.Targeted:= True;
end;

function BlessSpell: TSpell;
begin
  Result.SpellName:= 'Bless';
  Result.Regs:= [GA, MR];
  Result.ManaRequired := 9;
  Result.Harmfull := False;
  Result.Buff:= True;
  Result.Targeted:= True;
end;

function CunningSpell: TSpell;
begin
  Result.SpellName:= 'Cunning';
  Result.Regs:= [MR, NS];
  Result.ManaRequired := 6;
  Result.Harmfull := False;
  Result.Buff:= True;
  Result.Targeted:= True;
end;

function StrengthSpell: TSpell;
begin
  Result.SpellName:= 'Strength';
  Result.Regs:= [MR, NS];
  Result.ManaRequired := 6;
  Result.Harmfull := False;
  Result.Buff:= True;
  Result.Targeted:= True;
end;


function AgilitySpell: TSpell;
begin
  Result.SpellName:= 'Agility';
  Result.Regs:= [BM, MR];
  Result.ManaRequired := 6;
  Result.Harmfull := False;
  Result.Buff:= True;
  Result.Targeted:= True;
end;