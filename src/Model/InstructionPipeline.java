
package Model;

import java.util.ArrayList;

public class InstructionPipeline
{
    // Instruction # or index in the pipeline.
    private int index;
    
    // Variables relating to the register being edited by the instruction if any (e.g. OR, DADDIU; not BNE, J)
    public long result; // Should contain final result of register after instruction. This is assigned to as soon as possible. (e.g. EX stage for OR, MEM for LD)
    public Boolean resultAvailable; // Boolean that tells if the current "result" variable is updated.
    public Boolean resultAvailableNextStep; // Boolean that tells if the "result" variable will be updated after next step.
    
    // Instruction-related information
    private Opcode opcode;
    private DecodedInstruction inst;
    private InstructionCategory category;
    
    // Reference to Processor instance
    private Processor p;
    
    // What step this instruction is currently on: IF, ID, EX, MEM, WB
    private Step currentStep;
    
    // Error if any
    private ErrorLogData error;
    
    public InstructionPipeline(int index, Processor processor)
    {
        this.index = index;
        
        result = 0;
        resultAvailable = false;
        resultAvailableNextStep = false;
        
        inst = null;
        category = null;
        this.p = processor;
        currentStep = null;
        error = null;
    }
    
    public Boolean nextStep()
    {
        if (currentStep == null)
        {
            currentStep = Step.IF;
            return stepIF();
        }
        else
        {
            switch (currentStep)
            {
                case IF: currentStep = Step.ID; return stepID();
                case ID: currentStep = Step.EX; return stepEX();
                case EX: currentStep = Step.MEM; return stepMEM();
                case MEM: currentStep = Step.WB; return stepWB();
                case WB: currentStep = Step.END;
                default: return false;
            }
        }
    }
    
    private Boolean stepIF()
    {
        long PC = p.irs.getPC();
        if (p.opcodes.containsKey((int)PC))
        {
            opcode = p.opcodes.get((int)PC);
            if (opcode != null)
            {
                inst = DecodedInstruction.decodeInstruction(opcode);
                category = InstructionCategory.getCategoryOfInstruction(inst.type);
                if (inst.type == Instruction.BNE)
                {
                    if (checkRegisterAvailability(inst.rs) <= 0)
                    {
                        p.stallWaitingOn.add(inst.rs);
                        p.setStallingInstruction(index);
                    }
                    if (checkRegisterAvailability(inst.rt) <= 0)
                    {
                        p.stallWaitingOn.add(inst.rt);
                        p.setStallingInstruction(index);
                    }
                }
                long NPC = calculateNPC(PC);
                p.irs.setPC(NPC);
                p.irs.setIfid_NPC(NPC);
                p.irs.setIfid_IR(opcode);
                if (checkIfInstructionShouldHaveResult())
                {
                    ArrayList<Integer> adder = p.registerToUsers.get(inst.rd);
                    adder.add(index);
                    p.registerToUsers.replace(inst.rd, adder);
                }
                return true;
            }
        }
        return false;
    }
    
    private Boolean stepID()
    {
        p.irs.setIdex_IR(p.irs.getIfid_IR());
        p.irs.setIdex_A(p.db.getRegister(Converter.binaryToInt(opcode.getIR6_10())));
        p.irs.setIdex_B(p.db.getRegister(Converter.binaryToInt(opcode.getIR11_15())));
        p.irs.setIdex_Imm(p.db.getRegister(Converter.binaryToInt(opcode.getIR16_31())));
        if (category == InstructionCategory.ALU || category == InstructionCategory.Memory || inst.type == Instruction.BNE)
        {
            //Code: Stall if needed, get if ready.
            int availabilityRS = checkRegisterAvailability(inst.rs);
            if (availabilityRS <= 0)
            {
                p.stallWaitingOn.add(inst.rs);
                p.setStallingInstruction(index);
            }
            else if (availabilityRS == 1)
            {
                p.irs.setIdex_A(getRegisterValue(inst.rs));
            }

            if (category == InstructionCategory.ALU)
            {
                int availabilityRT = checkRegisterAvailability(inst.rt);
                if (availabilityRT <= 0)
                {
                    p.stallWaitingOn.add(inst.rt);
                    p.setStallingInstruction(index);
                }
                else if (availabilityRT == 1)
                {
                    p.irs.setIdex_B(getRegisterValue(inst.rt));
                }
                
                resultAvailableNextStep = true;
            }
        }
        return true;
    }
    
    private Boolean stepEX()
    {
        p.irs.setExmem_IR(p.irs.getIdex_IR());
        p.irs.setExmem_B(p.irs.getIdex_B());
        if (category == InstructionCategory.ALU)
        {
            //Calculate result here and update p.irs; OR sample is given.
            if (inst.type == Instruction.OR)
            {
                result = getRegisterValue(inst.rs) | getRegisterValue(inst.rt);
                p.irs.setExmem_ALU(result);
                p.irs.setExmem_Cond(0);
            }
            resultAvailable = true;
        }
        else if (inst.type == Instruction.LD)
        {
            //Do necesary LD ALU operation here.
            resultAvailableNextStep = true;
        }
        else if (inst.type == Instruction.SD)
        {
            //Do necesary SD ALU operation here.
            //Code: Stall if needed, get if ready.
            int availabilityRD = checkRegisterAvailability(inst.rd);
            if (availabilityRD <= 0)
            {
                p.stallWaitingOn.add(inst.rd);
                p.setStallingInstruction(index);
            }
            else if (availabilityRD == 1)
            {
                p.irs.setExmem_B(getRegisterValue(inst.rd));
            }
        }
        else if (inst.type == Instruction.BNE)
        {
            //Do BNE ALU here.
        }
        else if (inst.type == Instruction.J)
        {
            //Do J ALU here.
        }
        return true;
    }
    
    private Boolean stepMEM()
    {
        p.irs.setMemwb_IR(p.irs.getMemwb_IR());
        p.irs.setMemwb_ALU(p.irs.getExmem_ALU());
        if (inst.type == Instruction.LD)
        {
            //Set result here and update p.irs
            resultAvailable = true;
        }
        else if (inst.type == Instruction.SD)
        {
            //Perform SD here. 
            //Add code to get rd's register value or Ex/Mem B's value.
        }
        return true;
    }
    
    private Boolean stepWB()
    {
        if (checkIfInstructionShouldHaveResult())
        {
            p.db.setRegister(inst.rd, result);
            if (p.registerToUsers.containsKey(inst.rd) && p.registerToUsers.get(inst.rd).contains(index))
                p.registerToUsers.get(inst.rd).remove((Integer)index);
            p.irs.setRegAff(inst.rd);
        }
        return true;
    }
    
    public ErrorLogData getError()
    {
        return error;
    }
    
    /**
     * This function only to be used in IF instruction.
     */
    private long calculateNPC(long PC)
    {
        long NPC = PC + 0x0004;
        if (p.irs.getIfid_IR() != null)
        {
            DecodedInstruction prev = DecodedInstruction.decodeInstruction(p.irs.getIfid_IR());
            switch (prev.type)
            {
                case BNE:
                    if (getRegisterValue(prev.rs) != getRegisterValue(prev.rt))
                    {
                        if (p.db.instructionExists((int)(PC + prev.immediate * 4)))
                        {
                            NPC = PC + prev.immediate * 4;
                        }
                    }
                    break;
                case J:
                    if (p.db.instructionExists((int)(prev.memory * 4)))
                    {
                        NPC = prev.memory * 4;
                    }
            }
        }
        return NPC;
    }

    /**
     * @return 0 - Not available, 1 - Available, 2 - Available next step
     */
    private int checkRegisterAvailability(int register)
    {
        if (p.registerToUsers.containsKey(register))
        {
            ArrayList<Integer> users = p.registerToUsers.get(register);
            if (!users.isEmpty())
            {
                int lastUser = users.get(users.size() - 1);
                if (lastUser == index) 
                {
                    if (users.size() > 1)
                    {
                        lastUser = users.get(users.size() - 1);
                    }
                    else
                    {
                        return 1;
                    }
                }
                if (p.pipeline.containsKey(lastUser))
                {
                    if (p.pipeline.get(lastUser).resultAvailable) return 1;
                    else if (p.pipeline.get(lastUser).resultAvailableNextStep) return 2;
                    else return 0;
                }
            }
            else
            {
                return 1;
            }
        }
        else
        {
            return 1;
        }
        return 0;
    }
    
    /**
     * Be sure that you are using a valid register. This function does not check if
     * the register is updated or not based on pipeline.
     */
    private long getRegisterValue(int register)
    {
        if (p.registerToUsers.containsKey(register))
        {
            ArrayList<Integer> users = p.registerToUsers.get(register);
            if (!users.isEmpty())
            {
                int lastUser = users.get(users.size() - 1);
                if (lastUser == index && users.size() > 1) 
                {
                    lastUser = users.get(users.size() - 1);
                }
                if (p.pipeline.containsKey(lastUser))
                {
                    return p.pipeline.get(lastUser).result;
                }
            }
        }
        return (long)p.db.getRegister(register);
    }
    
    public Boolean checkIfInstructionShouldHaveResult()
    {
        return inst.type == Instruction.OR || inst.type == Instruction.DSUBU ||
            inst.type == Instruction.SLT || inst.type == Instruction.LD ||
            inst.type == Instruction.DADDIU;
    }
    
    public Instruction getInstructionType()
    {
        return inst.type;
    }
    
    public Step getCurrentStep()
    {
        return currentStep;
    }
}