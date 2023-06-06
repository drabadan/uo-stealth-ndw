program AnimalLore;

const
  GUMP_ID = $1DB;
var
  ANIMAL_TYPES: array of word = [$317];
  i: integer;
begin
  FindDistance := 1;
  for i := 0 to GetGumpsCount - 1 do
    CloseSimpleGump(i);
  while not Dead do
  begin
    FindTypesArrayEx(ANIMAL_TYPES, [$FFFF], [Ground], False);
    if FindItem = 0 then
      AddToSystemJournal('Animal not found');
    WaitTargetObject(FindItem);
    UseSkill('Animal Lore');
    if NOT (WaitJournalLine(Now, 'think of anything', 1500)) then
      begin
        // Wait(1000);
        for i := 0 to GetGumpsCount - 1 do
          CloseSimpleGump(i);
    end;
  end;
end.
