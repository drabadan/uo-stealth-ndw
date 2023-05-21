function GetRawFishSteaksCondition(const state: TState): boolean;
begin
  // FindType(RAW_FISH_STEAK_TYPE, Backpack);
  // Result := FindFullQuantity = 0;
  Result := True;
end;

function GetFishingPole(): Cardinal;
begin
  Result := 0;
  if GetType(ObjAtLayerEx(LhandLayer, Self)) = FISHING_POLE_TYPE then
    begin
      Result := ObjAtLayerEx(LhandLayer, Self);
      Exit;
    end
  else if (FindType(FISHING_POLE_TYPE, Backpack)) > 0 then
    begin
      Result := FindItem;
      Exit;
    end;
end;

procedure CutFish();
var
  currFish: Cardinal;
begin
  while FindTypesArrayEx(FISH_TYPES, [$FFFF], [Backpack], true) > 0 do
    begin
      currFish := FindItem;
      if NOT (FindTypesArrayEx(CUT_FISH_TOOLS, [$FFFF], [Backpack], true) > 0) then
        begin
          AddToSystemJournal('[Critical] Cannot find any cut fish tool!');
          Exit;
        end;
      WaitTargetObject(currFish);
      UseObject(FindItem);
      Wait(500);
    end;
end;

procedure GetRawFishSteaksExecutor(var state: TState; const command: TCommand);
const
  DISTANCE = 4;
var
  x, y: Integer;
  fishingPoleSerial: Cardinal;
begin
  // AddToSystemJournal('[INFO] Going fishing!');
  NewMoveXY(state.FishingPoint.X, state.FishingPoint.Y, true, 0, true);

  for x := DISTANCE * -1 to DISTANCE do
    for y := DISTANCE * -1 to DISTANCE do
      begin
        if TargetPresent then CancelTarget;
        fishingPoleSerial := GetFishingPole();
        if NOT fishingPoleSerial > 0 then
          begin
            AddToSystemJournal('[Critical] No fishing pole!');
            Exit;
          end;
        WaitTargetXYZ(GetX(Self) + x, GetY(Self) + y, GetZ(Self));
        UseObject(fishingPoleSerial);

        if NOT WaitJournalLine(Now, FISHING_MSG, MAX_FISHING_TIME_MS) then
          AddToSystemJournal('[Critical] Failed to get fishing journal message!');

        CutFish();        
        if FindType(RAW_FISH_STEAK_TYPE, Backpack) > 0 then
          begin            
            Exit;
          end;
      end;
end;

function GetRawFishSteaksAction(): TCommand;
begin
  Result.Title := 'Get raw fish steaks';
  Result.Condition := @GetRawFishSteaksCondition;
  Result.Executor := @GetRawFishSteaksExecutor;
  Result.Priority := LowPrio;
end;