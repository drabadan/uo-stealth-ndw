Program Peacemaking;

{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}

const PEACEMAKING_DELAY = 10000;

var
  MUSICAL_INSTRUMENTS: Array of Word = [$EB3];
  cTime: TDateTime;
  m1, m2, i: Integer;
begin
  While Not Dead do 
    begin
      FindTypesArrayEx(MUSICAL_INSTRUMENTS, [$FFFF], [Backpack], false);
      if FindItem = 0 then begin AddToSystemJournal('No musical instruments found!'); Break; end;
      UseSkill('Peacemaking');
      ClearJournal;
      cTime := Now;
      for i := 0 to 12 do
        begin
          Wait(1000);

          if (InJournalBetweenTimes('do you wish to calm', cTime, Now) <> -1) then
            begin
              TargetToObject(Self);
              Wait(PEACEMAKING_DELAY);
              Break;
            end;

          if (InJournalBetweenTimes('instrument you are trying to play is', cTime, Now) <> -1) then
            begin
              TargetToObject(FindItem);
              Wait(1000);
              TargetToObject(Self);
              Wait(PEACEMAKING_DELAY);
              Break;
            end;
          AddToSystemJournal('[Critical] no target received!');
        end;
    end;
end.