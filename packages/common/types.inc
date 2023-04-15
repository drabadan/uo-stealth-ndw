type
  TSpell = record
    SpellName: String;
    Regs: array of Word;
    ManaRequired: Word;
    Harmfull: boolean;
    Buff: boolean;
    Targeted: boolean;
  end;
  TSpellArray = Array of TSpell;

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

var SpellsCollection: TSpellArray = [DispelSpell, BlessSpell, CunningSpell, StrengthSpell, AgilitySpell];

function FindSpellByName(SpellName: String): TSpell;
var i: Integer;
begin
  for i:= SpellsCollection.Low to SpellsCollection.High do
      if SpellsCollection[i].SpellName = SpellName then
        begin
          Result := SpellsCollection[i];
          Exit;
        end;
  AddToSystemJournal('[WARNING] Unknown spell name: ' + SpellName);
end;