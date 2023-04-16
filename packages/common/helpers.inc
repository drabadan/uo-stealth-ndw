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