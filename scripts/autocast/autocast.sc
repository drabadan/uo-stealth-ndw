program Autocast;

{$I 'state.type.inc'}
{$I 'constants.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\common\types.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\core\queue.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\cast-spell.inc'}
{$I 'C:\Users\49160\dev\uo\scripts\stealth\pascal\packages\actions\magery\buff-spells.inc'}

var
  actions: TCommandArray;
  i: integer;

function SpeechEvToBuffTargetMapper(Text, SenderName: String; SenderID: Cardinal): TBuffTarget;
begin  
  Result.Serial := SenderID;
  Result.Phrase := Text;
  Result.SenderName := SenderName;
end;

procedure UnicodeSpeech(Text, SenderName: String; SenderID: Cardinal); 
begin   
  AddToSystemJournal('Event! Unicode Speech: SenderID = $'+ IntToHex(SenderID,8) + ' ; SenderName =  ' + SenderName + '; SenderText : ' + text); 
  if Text in SPEECH_PATTERNS then
    gState.BuffTargets.Push(SpeechEvToBuffTargetMapper(Text, SenderName, SenderID));
end; 

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

begin
  SetEventProc(evSpeech,'UnicodeSpeech');
  RegisterActions([
    CastBuffsAction()
  ]);
  RunUntilCancel(gState);
end.
