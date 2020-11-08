function initializeCoreMod() {
    print("Topography Core Mod Initializing");
    return {
        'chunkstatussurface': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.chunk.ChunkStatus',
                'methodName': 'lambda$static$6',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)V'
            },
            'transformer': function(method) {
                print("Topography injecting into: " + method.name);
            	
            	var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
            	var Opcodes = Java.type('org.objectweb.asm.Opcodes');
            	var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
            	var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
            	var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
            	var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
            	var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
            	
            	var target = ASMAPI.findFirstInstruction(method, Opcodes.ALOAD);
        		
        		if (target == null)
        		{
        			throw "Something went wrong in Topography";
        		}
            	
            	var toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
            	toInject.add(ASMAPI.buildMethodCall("com/bloodnbonesgaming/topography/common/core/Hooks", "onChunkStatusSurface", "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)Z", ASMAPI.MethodType.STATIC));
            	var label = new LabelNode();
        		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        		toInject.add(new InsnNode(Opcodes.RETURN));
        		toInject.add(label);

        		method.instructions.insertBefore(target, toInject);
        		
                return method;
            }
        },
        'chunkstatusnoise': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.chunk.ChunkStatus',
                'methodName': 'lambda$static$5',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)V'
            },
            'transformer': function(method) {
                print("Topography injecting into: " + method.name);
            	
            	var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
            	var Opcodes = Java.type('org.objectweb.asm.Opcodes');
            	var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
            	var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
            	var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
            	var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
            	var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
            	
            	var target = ASMAPI.findFirstInstruction(method, Opcodes.NEW);
        		
        		if (target == null)
        		{
        			throw "Something went wrong in Topography";
        		}
            	
            	var toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
            	toInject.add(ASMAPI.buildMethodCall("com/bloodnbonesgaming/topography/common/core/Hooks", "onChunkStatusNoise", "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)Z", ASMAPI.MethodType.STATIC));
            	var label = new LabelNode();
        		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        		toInject.add(new InsnNode(Opcodes.RETURN));
        		toInject.add(label);

        		method.instructions.insertBefore(target, toInject);
        		
                return method;
            }
        },
        'chunkstatuscarvers': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.chunk.ChunkStatus',
                'methodName': 'lambda$static$7',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)V'
            },
            'transformer': function(method) {
                print("Topography injecting into: " + method.name);
            	
            	var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
            	var Opcodes = Java.type('org.objectweb.asm.Opcodes');
            	var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
            	var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
            	var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
            	var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
            	var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
            	
            	var target = ASMAPI.findFirstInstruction(method, Opcodes.ALOAD);
        		
        		if (target == null)
        		{
        			throw "Something went wrong in Topography";
        		}
            	
            	var toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
            	toInject.add(ASMAPI.buildMethodCall("com/bloodnbonesgaming/topography/common/core/Hooks", "onChunkStatusCarvers", "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)Z", ASMAPI.MethodType.STATIC));
            	var label = new LabelNode();
        		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        		toInject.add(new InsnNode(Opcodes.RETURN));
        		toInject.add(label);

        		method.instructions.insertBefore(target, toInject);
        		
                return method;
            }
        },
        'chunkstatusliquidcarvers': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.world.chunk.ChunkStatus',
                'methodName': 'lambda$static$8',
                'methodDesc': '(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)V'
            },
            'transformer': function(method) {
                print("Topography injecting into: " + method.name);
            	
            	var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
            	var Opcodes = Java.type('org.objectweb.asm.Opcodes');
            	var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
            	var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
            	var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
            	var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
            	var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
            	
            	var target = ASMAPI.findFirstInstruction(method, Opcodes.ALOAD);
        		
        		if (target == null)
        		{
        			throw "Something went wrong in Topography";
        		}
            	
            	var toInject = new InsnList();
            	
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 2));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 3));
            	toInject.add(ASMAPI.buildMethodCall("com/bloodnbonesgaming/topography/common/core/Hooks", "onChunkStatusLiquidCarvers", "(Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/world/gen/ChunkGenerator;Ljava/util/List;Lnet/minecraft/world/chunk/IChunk;)Z", ASMAPI.MethodType.STATIC));
            	var label = new LabelNode();
        		toInject.add(new JumpInsnNode(Opcodes.IFEQ, label));
        		toInject.add(new InsnNode(Opcodes.RETURN));
        		toInject.add(label);

        		method.instructions.insertBefore(target, toInject);
        		
                return method;
            }
        },
        'minecraftloadworld': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.Minecraft',
                'methodName': 'func_238191_a_',
                'methodDesc': '(Ljava/lang/String;)V'
            },
            'transformer': function(method) {
                print("Topography injecting into: " + method.name);
            	
            	var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
            	var Opcodes = Java.type('org.objectweb.asm.Opcodes');
            	var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
            	var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
            	var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
            	var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
            	var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
            	
            	var target = ASMAPI.findFirstInstruction(method, Opcodes.ALOAD);
        		
        		if (target == null)
        		{
        			throw "Something went wrong in Topography";
        		}
            	
            	var toInject = new InsnList();

            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
            	toInject.add(new VarInsnNode(Opcodes.ALOAD, 1));
            	toInject.add(ASMAPI.buildMethodCall("com/bloodnbonesgaming/topography/common/core/Hooks", "onMinecraftLoadWorld", "(Lnet/minecraft/client/Minecraft;Ljava/lang/String;)V", ASMAPI.MethodType.STATIC));
        		toInject.add(new InsnNode(Opcodes.RETURN));

        		method.instructions.insertBefore(target, toInject);
        		
                return method;
            }
        }
    }
}