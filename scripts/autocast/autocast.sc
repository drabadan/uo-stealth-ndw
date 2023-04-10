program Autocast;

{$REGION 'Includes'}
{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\types.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\cast-spell.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\buff-spells.inc'}
{$ENDREGION}

var REGS_CONST: array[0..7] of Word = [BP, BM, MR, NS, SS, SA, GA, GS];

var
  actions: TCommandArray;
  i: integer;

{$REGION 'Restock'}
function RestockRegsCondition(const state: TState): boolean;
var 
  i, tq: Integer;
begin
  tq := 0;
  for i := REGS_CONST.Low to REGS_CONST.High do
    begin
      FindType(REGS_CONST[i], Backpack);
      tq := tq + FindFullQuantity;
    end;

  Result := tq >= (REGS_CONST.Length * state.MinPerRegQuantity);
end;

procedure RestockRegsExecutor(var state: TState; const command: TCommand);
var 
  i, d: Integer;
begin
  d := 0;
  for i := REGS_CONST.Low to REGS_CONST.High do
    begin
      FindType(REGS_CONST[i], Backpack);
      d := state.LoadRegsQuantity - FindFullQuantity;
      if NOT (MoveItem(FindType(REGS_CONST[i], Ground), d, Backpack, 0,0,0)) then
        AddToSystemJournal('[Critical] Cannot load Regs from floor: ', REGS_CONST[i]);
      Wait(MOVE_ITEM_DELAY_MS);
    end;
end;

function RestockRegs(): TCommand;
begin
  Result.Title := 'Restock Regs';
  Result.Condition := @RestockRegsCondition;
  Result.Executor := @RestockRegsExecutor;
  Result.Priority := HighPrio;
end;

{$ENDREGION}

{$REGION 'Cast-buff'}
function CastBuffCondition(const state: TState): boolean;
begin
  Result := state.BuffTargets.Length > 0;
end;

procedure CastBuffExecutor(var state: TState; const command: TCommand);
var 
  target: TBuffTarget;
  buffs: array of Char;
  i : Integer;
begin
  target := state.BuffTargets.Pop();
  buffs := StrSplit(target.Phrase, '');
  for i := buffs.Low to buffs.High do 
    begin
      AddToSystemJournal(buffs[i]);
      AddToSystemJournal(SPELLS_MATCH[StrToInt(buffs[i])]);
    end;

  // CancelRunning();
end;

function CastBuffsAction(): TCommand;
begin
  Result.Title := 'Cast buff';
  Result.Condition := @CastBuffCondition;
  Result.Executor := @CastBuffExecutor;
  Result.Priority := HighPrio;
end;
{$ENDREGION}

{$REGION 'Speech-manager'}
function SpeechEvToBuffTargetMapper(Text, SenderName: String; SenderID: Cardinal): TBuffTarget;
begin  
  Result.Serial := SenderID;
  Result.Phrase := Text;
  Result.SenderName := SenderName;
end;

procedure UnicodeSpeech(Text, SenderName: String; SenderID: Cardinal); 
begin     
  if Text in SPEECH_PATTERNS then
    gState.BuffTargets.Push(SpeechEvToBuffTargetMapper(Text, SenderName, SenderID));
end; 
{$ENDREGION}

{$REGION 'Check Mana'}
function CheckManaCondition(const state: TState): boolean;
begin
  Result := Mana <= state.CriticalManaValue;
end;

procedure CheckManaExecutor(var state: TState; const command: TCommand);
var 
  medTimer: TDateTime;
  ma, mb: Integer;  
begin
  while Mana <> Int do
    begin
      medTimer := now;
      UseSkill('Meditation');
      repeat
        ma := InJournalBetweenTimes(MEDITATION_FAIL_MSG, medTimer, Now);
        mb := InJournalBetweenTimes(MEDITATION_FINISH_MSG, medTimer, Now);
        wait(100);        
      until (ma >= 0) or (mb >= 0) or (Now > (medTimer + (0.168 / 1440)));
      if (ma >= 0) then Wait(10500);
      Wait(100);
    end;
end;

function CheckManaAction(): TCommand;
begin
  Result.Title := 'Check Mana';
  Result.Condition := @CheckManaCondition;
  Result.Executor := @CheckManaExecutor;
  Result.Priority := HighPrio;
end;

{$ENDREGION}

{$REGION 'Check And Cast Self'}
function CheckCastSelfCondition(const state: TState): boolean;
begin
  Result := Int < state.MinIntValue;
end;

procedure CheckCastSelfExecutor(var state: TState; const command: TCommand);
begin
  CastSpell(DispelSpell, Self);
  CastSpell(BlessSpell, Self);
  CastSpell(CunningSpell, Self);
  CastSpell(StrengthSpell, Self);
  CastSpell(AgilitySpell, Self);
end;

function CheckCastSelfAction(): TCommand;
begin
  Result.Title := 'Check And Cast Self';
  Result.Condition := @CheckCastSelfCondition;
  Result.Executor := @CheckCastSelfExecutor;
  Result.Priority := HighPrio;
end;

{$ENDREGION}

function InitState(): TState;
begin
  // Minimum REG count to restock
  Result.MinPerRegQuantity := 10;
  // How many regs to take from the floor
  Result.LoadRegsQuantity := 50;
  Result.MinIntValue := 135;
  Result.CriticalManaValue := 55;
end;

begin
  SetEventProc(evSpeech,'UnicodeSpeech');
  RegisterActions([
    RestockRegs(),
    CheckManaAction(),    
    CheckCastSelfAction(),
    CastBuffsAction()
  ]);
  RunUntilCancel(gState);
end.

