program Autocast;

{$REGION 'Includes'}
{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\types.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\cast-spell.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\buff-spells.inc'}
{$I 'Actions.inc'}
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

