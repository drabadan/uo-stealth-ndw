Program Bushido;
begin
  while not Dead do
    begin
      Cast('Confidence');
      // WaitJournalLine(Now, 'no longer feel that you could deflect', 10000);
      WaitJournalLine(Now, 'exude confidence', 10000);
      Wait(15000);
    end;
end.