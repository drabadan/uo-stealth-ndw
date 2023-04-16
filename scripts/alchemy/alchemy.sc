Program Alchemy;

{$REGION 'Includes'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\types.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\helpers.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\craftable-types.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\craftables\alchemy.inc'}
{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'actions.inc'}
{$ENDREGION}

function GetInitialState(): TState;
begin  
  Result.RunsCount := 0;
  Result.CurrentCraftable := DeadlyPoisonPotion;
  Result.PouringContainerGraphic := KEG_GRAPHIC;
  Result.CurrentPouringContainerId := 0;
  Result.MinPerRegQuantity := 10;
  // How many regs to take from the floor
  Result.LoadRegsQuantity := 50;
  Result.PouringContainerContainer := Backpack;
end;

begin
  gState := GetInitialState();
   RegisterActions([
    RestockRegs(),
    SetCurrentPouringContainerAction(),
    CraftOnePotionAction()
  ]);
  RunUntilCancel(gState);
end.