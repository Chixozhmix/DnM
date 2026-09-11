//package net.chixozhmix.dnmmod.network.packet;
//
//import net.chixozhmix.dnmmod.entity.modeus.ModeusBoss;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.Supplier;
//
//public class DangerZonesPacket {
//    private final int entityId;
//    private final List<DangerZoneData> zones;
//
//    public DangerZonesPacket(int entityId, List<DangerZoneData> zones) {
//        this.entityId = entityId; this.zones = zones;
//    }
//
//    public record DangerZoneData(double x, double y, double z, float rotation, float length, float width) {
//        public void encode(FriendlyByteBuf buffer) {
//            buffer.writeDouble(x);
//            buffer.writeDouble(y);
//            buffer.writeDouble(z);
//
//            buffer.writeFloat(rotation);
//            buffer.writeFloat(length);
//            buffer.writeFloat(width);
//        }
//
//        public static DangerZoneData decode(FriendlyByteBuf buffer) {
//            return new DangerZoneData(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
//        }
//    }
//
//    /** * Записываем пакет в буфер. */
//    public static void encode(DangerZonesPacket packet, FriendlyByteBuf buffer) {
//        buffer.writeInt(packet.entityId); buffer.writeInt(packet.zones.size());
//
//        for (DangerZoneData zone : packet.zones) {
//            zone.encode(buffer);
//        }
//    }
//
//    /** * Читаем пакет из буфера. */
//    public static DangerZonesPacket decode(FriendlyByteBuf buffer) {
//        int entityId = buffer.readInt(); int size = buffer.readInt();
//        List<DangerZoneData> zones = new ArrayList<>();
//
//        for (int i = 0; i < size; i++) {
//            zones.add(DangerZoneData.decode(buffer));
//        }
//
//        return new DangerZonesPacket(entityId, zones);
//    }
//
//    /** * Обработка пакета на клиенте. */
//    public static void handle(DangerZonesPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//
//        context.enqueueWork(() -> {
//            Level level = net.minecraft.client.Minecraft.getInstance().level;
//
//            if (level == null)
//                return;
//
//            Entity entity = level.getEntity(packet.entityId);
//
//            if (entity instanceof ModeusBoss modeus) {
//
//                List<ModeusBoss.DangerZonePosition> positions = packet.zones.stream()
//                                .map(zone -> new ModeusBoss.DangerZonePosition(zone.x(), zone.y(), zone.z(), zone.rotation(), zone.length(), zone.width())).toList();
//
//                modeus.setClientDangerZonePositions(positions);
//            }
//        });
//
//        context.setPacketHandled(true);
//    }
//}
