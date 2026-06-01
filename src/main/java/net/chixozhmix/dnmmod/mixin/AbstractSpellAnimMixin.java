//package net.chixozhmix.dnmmod.mixin;
//
//import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
//import io.redspace.ironsspellbooks.api.spells.CastType;
//import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
//import io.redspace.ironsspellbooks.api.util.AnimationHolder;
//import net.chixozhmix.dnmmod.api.spell.DnMSpellAnimations;
//import net.chixozhmix.dnmmod.api.spell.ItemTypeHelper;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//
////Нужно что-то делать с анимациями, пока работает очень плохо
//@Mixin(AbstractSpell.class)
//public abstract class AbstractSpellAnimMixin {
//    @Shadow
//    public abstract CastType getCastType();
//
//
//    @Overwrite(remap = false)
//    public AnimationHolder getCastStartAnimation() {
//        // Проверяем стек вызовов - применяем кастомную анимацию только если
//        if (isCalledForLocalPlayer()) {
//            Player localPlayer = Minecraft.getInstance().player;
//            if (localPlayer != null && ItemTypeHelper.isHoldingStaff(localPlayer)) {
//                return switch (this.getCastType()) {
//                    case INSTANT -> DnMSpellAnimations.WAND_INSTANT_CAST;
//                    case CONTINUOUS -> DnMSpellAnimations.WAND_CONTINUOUS_CAST;
//                    case LONG -> DnMSpellAnimations.WAND_LONG_CAST;
//                    default -> AnimationHolder.none();
//                };
//            }
//        }
//
//        // Стандартное поведение
//        return switch (this.getCastType()) {
//            case INSTANT -> SpellAnimations.ANIMATION_INSTANT_CAST;
//            case CONTINUOUS -> SpellAnimations.ANIMATION_CONTINUOUS_CAST;
//            case LONG -> SpellAnimations.ANIMATION_LONG_CAST;
//            default -> AnimationHolder.none();
//        };
//    }
//
//
//    @Overwrite(remap = false)
//    public AnimationHolder getCastFinishAnimation() {
//        if (isCalledForLocalPlayer()) {
//            Player localPlayer = Minecraft.getInstance().player;
//            if (localPlayer != null && ItemTypeHelper.isHoldingStaff(localPlayer)) {
//                if (this.getCastType() == CastType.LONG) {
//                    return DnMSpellAnimations.WAND_LONG_CAST_FINISH;
//                }
//                return DnMSpellAnimations.WAND_LONG_CAST_FINISH;
//            }
//        }
//
//        return switch (this.getCastType()) {
//            case INSTANT -> AnimationHolder.pass();
//            case LONG -> SpellAnimations.ANIMATION_LONG_CAST_FINISH;
//            default -> AnimationHolder.none();
//        };
//    }
//
//    /**
//     * Проверяет, что текущий вызов метода происходит для локального игрока.
//     * Анализирует стек вызовов на наличие методов, специфичных для игрока.
//     */
//    private static boolean isCalledForLocalPlayer() {
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//
//        for (StackTraceElement element : stackTrace) {
//            String className = element.getClassName();
//            String methodName = element.getMethodName();
//
//            // Если вызов идет от клиентского класса игрока, возвращаем true
//            if (className.contains("LocalPlayer") ||
//                    className.contains("ClientPlayer") ||
//                    methodName.contains("localPlayer") ||
//                    methodName.contains("clientPlayer")) {
//                return true;
//            }
//
//            // Если вызов идет от моба, возвращаем false
//            if (className.contains("AbstractSpellCastingMob") ||
//                    className.contains("SpellCastingMob")) {
//                return false;
//            }
//        }
//
//        // По умолчанию считаем, что вызов от игрока (безопасный вариант)
//        return true;
//    }
//}