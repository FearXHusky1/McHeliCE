package com.norwood.mcheli.core;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;

import static com.norwood.mcheli.core.MCHCore.coreLogger;

public class RenderGlobalTransformer implements IClassTransformer {

    private static final ObfSafeName RENDER_ENTITIES = new ObfSafeName("renderEntities", "func_180446_a");

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (!transformedName.equals("net.minecraft.client.renderer.RenderGlobal")) {
            return basicClass;
        }
        coreLogger.info("Patching class {} / {}", transformedName, name);
        try {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(basicClass);
            classReader.accept(classNode, 0);

            for (MethodNode method : classNode.methods) {
                if (RENDER_ENTITIES.matches(method.name) && method.desc
                        .equals("(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V")) {
                    coreLogger.info("Patching method: {} / {}", RENDER_ENTITIES.mcp, method.name);
                    patchRender(method);
                }
            }

            ClassWriter writer = new W_ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
        } catch (Throwable t) {
            MCHCore.fail("net.minecraft.client.renderer.RenderGlobal", t);
            return basicClass;
        }
    }

    private void patchRender(MethodNode method) {
        ListIterator<AbstractInsnNode> iter = method.instructions.iterator();

        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();

            if (insn.getOpcode() == Opcodes.ISTORE && insn instanceof VarInsnNode varNode) {
                if (varNode.var == 28) { // flag local index
                    InsnList inject = new InsnList();
                    inject.add(new VarInsnNode(Opcodes.ILOAD, 28));// load flag
                    inject.add(new VarInsnNode(Opcodes.ALOAD, 27));// load entity2
                    inject.add(new TypeInsnNode(
                            Opcodes.INSTANCEOF,
                            "com/norwood/mcheli/aircraft/MCH_EntityAircraft"
                    ));
                    inject.add(new InsnNode(Opcodes.ICONST_1));
                    inject.add(new InsnNode(Opcodes.IXOR));// !instanceof
                    inject.add(new InsnNode(Opcodes.IAND));// flag && !instanceof
                    inject.add(new VarInsnNode(Opcodes.ISTORE, 28)); // store back to flag

                    method.instructions.insert(insn, inject);
                    coreLogger.info("Injected aircraft render skip after flag assignment (istore 28)");
                    break;
                }
            }
        }
    }
}
