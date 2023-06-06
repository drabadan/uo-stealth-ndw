program StealFanDancer;

{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}

const
  TARGET_ID = $06D5A296;//$06CBC836;
begin
  while Not Dead and Connected and (GetSkillValue('Ninjitsu') < 120) do
    begin
      if Mana < 12 then Wait(5000);
      If Not Hidden then 
        begin
          UseSkill('Hiding');
          Wait(12000);
          UseSkill('Stealth');
          Wait(2000);
        end;

      while GetDistance(TARGET_ID) > 5 do NewMoveXY(GetX(TARGET_ID), GetY(TARGET_ID), true, 2, false);
      WaitTargetObject(TARGET_ID);      
      Cast('Shadowjump');
      Wait(3500);
    end;
end.