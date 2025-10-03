package com.norwood.mcheli.helper.info;

import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.item.MCH_ItemInfo;
import com.norwood.mcheli.plane.MCP_PlaneInfo;
import com.norwood.mcheli.ship.MCH_ShipInfo;
import com.norwood.mcheli.tank.MCH_TankInfo;
import com.norwood.mcheli.throwable.MCH_ThrowableInfo;
import com.norwood.mcheli.vehicle.MCH_VehicleInfo;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class YamlParser implements IParser {

    public static final Yaml YAML_INSTANCE = new Yaml();
    public static final YamlParser INSTANCE = new YamlParser();

    private YamlParser() {
    }

    public static void register() {
        ContentParsers.register("yml", INSTANCE);
    }

    @Override
    public @Nullable MCH_HeliInfo parseHelicopter(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        InputStream input = Files.newInputStream(Paths.get(filepath), StandardOpenOption.READ);
        Map<String, Object> root = YAML_INSTANCE.load(input);


    }

    @Override
    public @Nullable MCP_PlaneInfo parsePlane(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_ShipInfo parseShip(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_TankInfo parseTank(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_VehicleInfo parseVehicle(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_WeaponInfo parseWeapon(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_ThrowableInfo parseThrowable(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_Hud parseHud(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    @Override
    public @Nullable MCH_ItemInfo parseItem(AddonResourceLocation location, String filepath, List<String> lines, boolean reload) throws Exception {
        return null;
    }

    private void mapToAircraft(MCH_AircraftInfo info, Map<String, Object> root) {


        for (Map.Entry<String, Object> entry : root.entrySet()) {
            switch (entry.getKey()) {
                case "DisplayName" -> {
                    Object nameObject = entry.getValue();
                    if (nameObject instanceof String name)
                        info.displayName = name.trim();
                    else if (nameObject instanceof Map<?, ?> translationNames)
                        info.displayNameLang = (HashMap<String, String>) translationNames;
                    else throw new ClassCastException();
                }
                case "Author" -> {
                   //Proposal: would allow content creators to put their signature
                }
                //Depricated on 1,12, around for 1.7 compat
                case "ItemID" -> {
                    info.itemID = (int) entry.getValue();
                }
                case "Category" -> {
                    if (entry.getValue() instanceof String category)
                        info.category = category.toUpperCase(Locale.ROOT).trim();
                    if (entry.getValue() instanceof List<?> categories) {
                        List<String> list = (List<String>) categories;
                        info.category = list.stream()
                                .map(String::trim)
                                .map(String::toUpperCase)
                                .collect(Collectors.joining(","));
                    }

                }
                case "CanRide" -> info.canRide = (boolean) entry.getValue();
                case "CreativeOnly" -> info.creativeOnly = (boolean) entry.getValue();
                case "Invulnerable" -> info.invulnerable = (boolean) entry.getValue();
                case "MaxFuel" -> info.maxFuel = getClamped(0, 100_000_000, (Number) entry.getValue());
                case "FuelConsumption" -> info.fuelConsumption = getClamped(0.0F, 10_000.0F, (Number) entry.getValue());
                case "FuelSupplyRange" -> info.fuelSupplyRange = getClamped(0F, 1_1000.0F, (Number) entry.getValue());
                case "AmmoSupplyRange" -> info.ammoSupplyRange = getClamped(1000, (Number) entry.getValue());
                case "RepairOtherVehicles" -> {
                    Map<String, Number> repairMap = (HashMap<String, Number>) entry.getValue();
                    if (repairMap.containsKey("range"))
                        info.repairOtherVehiclesRange = getClamped(1_000.0F, repairMap.get("range"));
                    if (repairMap.containsKey("value"))
                        info.repairOtherVehiclesValue = getClamped(10_000_000, repairMap.get("value"));
                }

                //UNUSED in reforged too,
//                case "RadarType" -> {
//                    if (entry.getValue() instanceof String data) {
//                        try {
//                            info.radarType = EnumRadarType.valueOf(data);
//                        } catch (IllegalArgumentException e) {
//                            info.radarType = EnumRadarType.MODERN_AA;
//                        }
//                    }
//                }
//                case "RWRType" -> {
//                    if (entry.getValue() instanceof String data) {
//                        try {
//                            info.rwrType = EnumRWRType.valueOf(data);
//                        } catch (IllegalArgumentException e) {
//                            info.rwrType = EnumRWRType.DIGITAL;
//                        }
//                    }
//                }
                case "NameOnModernAARadar" -> info.nameOnModernAARadar = ((String) entry.getValue()).trim();
                case "NameOnEarlyAARadar" -> info.nameOnEarlyAARadar = ((String) entry.getValue()).trim();
                case "NameOnModernASRadar" -> info.nameOnModernASRadar = ((String) entry.getValue()).trim();
                case "NameOnEarlyASRadar" -> info.nameOnEarlyASRadar = ((String) entry.getValue()).trim();
                case "ExplosionSizeByCrash" -> info.explosionSizeByCrash = getClamped(100, (Number) entry.getValue());
                case "ThrottleDownFactor" -> {
                    if (entry.getValue() instanceof String data)
                        info.throttleDownFactor = info.toFloat(data, 0, 10);
                }
                case "HUDType", "WeaponGroupType" -> {
                    //Unimplemented
                }
                case "Textures" -> {
                    List<String> textures = (List<String>) entry.getValue();
                    textures.stream().map(String::trim).forEach(info::addTextureName);
                }
                case "ParticleScale" -> info.particlesScale = getClamped(50f, (Number) entry.getValue());

                case "EnableSeaSurfaceParticle" -> info.enableSeaSurfaceParticle = (boolean) entry.getValue();


            }
        }

    }

    private float getClamped(float min, float max, Number value) {
        return Math.max(min, Math.min(max, value.floatValue()));
    }

    private int getClamped(int min, int max, Number value) {
        return Math.max(min, Math.min(max, value.intValue()));
    }

    private double getClamped(double min, double max, Number value) {
        return Math.max(min, Math.min(max, value.doubleValue()));
    }

    private float getClamped(float max, Number value) {
        return getClamped(0, max, value);
    }

    private int getClamped(int max, Number value) {
        return getClamped(0, max, value);
    }

    private double getClamped(double max, Number value) {
        return getClamped(0, max, value);
    }


    private boolean valOrDefault(Object object, boolean defValue) {
        return object instanceof Boolean bool ? bool : defValue;
    }
}
