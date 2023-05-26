function GetFoundItemsFromStringList(var Items: Array Of Cardinal): Integer;
var List: TStringList; i: Integer;
begin
  List := TStringList.Create;
  if GetFindedList(List) = False then Result := 0
  else begin
    SetLength(Items, List.Count);
    for i := 0 to Length(Items)-1 do Items[i] := StrToInt('$'+List.Strings[i]);
    Result := Length(Items);
  end;
  List.Free;
end;

function WaitGumpMaxMS(WaitTimeMS: Word; GumpID: Cardinal): boolean;
const MIN_WAIT_TIME_MS = 100;
var
  i, k: Integer;
begin
  for i := 0 to Integer((WaitTimeMS / MIN_WAIT_TIME_MS)) do
    begin
      for k:=0 to GetGumpsCount-1 do
        if GetGumpID(k) = GumpID then
          begin
            Result := True;
            Exit;
          end;
      Wait(MIN_WAIT_TIME_MS);
    end;    
  Result := False;
  AddToSystemJournal('[Warning] Gump: ', GumpID, ' not found!');
end;

procedure CloseAllGumps();
var i: Integer;
begin
  for i:=0 to GetGumpsCount-1 do
    CloseSimpleGump(i);
end;