Program AnimalTaming;
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}

var TAMABLES: Array of Word = [
    $D5, // Polar bear 35+
    $DD // Walrus 35+
    ];

var creatures: Array of Cardinal;
var creaturesIndex, waypointIndex: Integer;
var Waypoints: Array of TPoint;

procedure InitWaypoints();
var tmpPoint: TPoint;
begin
  tmpPoint.X := 4110;
  tmpPoint.Y := 440;
  Waypoints.Add(tmpPoint);

  tmpPoint.X := 4074;
  tmpPoint.Y := 440;
  Waypoints.Add(tmpPoint);

  tmpPoint.X := 4046;
  tmpPoint.Y := 440;
  Waypoints.Add(tmpPoint);

  tmpPoint.X := 4072;
  tmpPoint.Y := 476;
  Waypoints.Add(tmpPoint);
end;

function ReleaseCreature(Creature: Cardinal): boolean;
begin
  if NOT IsObjectExists(Creature) then Exit(True);
  NewMoveXY(GetX(Creature), GetY(Creature), true, 2, true);
  SetContextMenuHook(Creature, 9);
  RequestContextMenu(Creature);
  Wait(1500);
  NumGumpButton(GetGumpsCount-1, 2);
end;

function TameCreature(Creature: Cardinal): boolean;
var
  cTime: TDateTime;  
  i: Integer;
begin
  Result := False;
  NewMoveXY(GetX(Creature), GetY(Creature), true, 2, true);
  WaitTargetObject(Creature);  
  UseSkill('Animal Taming');
  cTime:= Now;
  for i := 0 to 15 do
    begin
      Wait(1000);
      NewMoveXY(GetX(Creature), GetY(Creature), true, 2, true);
      if (InJournalBetweenTimes('fail to tame the|too far away', cTime, Now) <> -1) then Exit(False);
      if (InJournalBetweenTimes('to accept you as master|animal looks tame already', cTime, Now) <> -1) then Exit(True);      
  end;
end;

begin
  InitWaypoints();
  FindDistance := 20;
  while NOT Dead do
    begin
      for waypointIndex := 0 to High(Waypoints) do
        begin
          NewMoveXY(Waypoints[waypointIndex].X, Waypoints[waypointIndex].Y, true, 1, true);
          FindTypesArrayEx(TAMABLES, [$FFFF], [Ground], false);
          GetFoundItemsFromStringList(creatures);
          for creaturesIndex := 0 to High(creatures) do            
            while Not Dead And IsObjectExists(creatures[creaturesIndex]) do 
              begin
                Wait(100);
                if (TameCreature(creatures[creaturesIndex])) then
                  begin
                    ReleaseCreature(creatures[creaturesIndex]);
                    Ignore(creatures[creaturesIndex]);
                    AddToSystemJournal('Finished taming: ', creatures[creaturesIndex]);                
                    Break;
                  end;
              end;
        end;
    end;
end.