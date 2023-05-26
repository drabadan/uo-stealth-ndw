Program FishingCooking;

{$REGION 'Includes'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}
{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'actions.inc'}
{$ENDREGION}

function InitFields: Array of TField;
var tmp: TField;
begin
  tmp.CenterPoint.X := 1153;
  tmp.CenterPoint.Y := 1577;
  Result.Add(tmp);
  tmp.CenterPoint.X := 1119;
  tmp.CenterPoint.Y := 1627;
  Result.Add(tmp);
  tmp.CenterPoint.X := 1136;
  tmp.CenterPoint.Y := 1792;
  Result.Add(tmp);
  tmp.CenterPoint.X := 1200;
  tmp.CenterPoint.Y := 1823;
  Result.Add(tmp);
  tmp.CenterPoint.X := 1232;
  tmp.CenterPoint.Y := 1886;
  Result.Add(tmp);
end;

function GetInitialState(): TState;
begin  
  Result.RunsCount := 0;
  Result.Fields := InitFields;  
  Result.CurrentFieldIndex := 0;
  Result.UnloadQuantity:= 40;
  Result.GatherTypes:= [$C58, $C57, $0C56, $C55];
  Result.LootTypes:= [$1EBD];
  Result.FlourMillPoint.X := 1277;
  Result.FlourMillPoint.Y := 1426;
  Result.UnloadChestId := $459A3189;
  Result.Run := True;
end;

begin
  MoveOpenDoor := true;
  gState := GetInitialState();
  RegisterActions([
    // should always be hidden
    // HideAction(),
    // has more then 10 sacks or 40 raw wheat
    // MillAction(),
    // scan and decide to move to next field or stay
    ScanFieldAction(),
    // gather the current field
    GatherTheResourcesAction()
  ]);
  RunUntilCancel(gState);
end.