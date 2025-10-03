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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
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
                case "displayName" -> {
                    Object nameObject = entry.getValue();
                    if (nameObject instanceof String name)
                        info.displayName = name;
                   else if (nameObject instanceof Map<?, ?> translationNames)
                        info.displayNameLang = (HashMap<String, String>) translationNames;
                    else throw new ClassCastException();
                }
                //Depricated on 1,12, around for 1.7 compat
                case "itemID" -> {
                    if (entry.getValue() instanceof Integer integer)
                        info.itemID = integer;
                    else throw new RuntimeException("Item ID must be an integer!");
                }
                case "Category" -> {
                    if (entry.getValue() instanceof String category)
                        info.category = category.toUpperCase(Locale.ROOT);
                    if (entry.getValue() instanceof String[] categories)
                        info.category = Arrays.stream(categories)
                                .map(String::toUpperCase)
                                .collect(Collectors.joining(","));


                }
                case "CanRide" -> info.canRide = valOrDefault(entry.getValue(), true);
                case "CreativeOnly" -> info.canRide = valOrDefault(entry.getValue(), false);
                case "Invulnerable" -> info.invulnerable = valOrDefault(entry.getValue(), false);
                case "MaxFuel" -> {
                    if (entry.getValue() instanceof Number num)
                        info.maxFuel = getClamped(0, 100_000_000, num);

                }
                case "FuelConsumption" -> {
                    if (entry.getValue() instanceof Number num)
                        info.fuelConsumption = getClamped(0.0F, 10_000.0F, num);
                }
                case "FuelSupplyRange" -> {
                    if (entry.getValue() instanceof Number num)
                        info.fuelSupplyRange = getClamped(0F, 1_1000.0F, num);
                }
                case "AmmoSupplyRange" -> {
                    if (entry.getValue() instanceof Number num)
                        info.ammoSupplyRange = getClamped(1000, num);
                }
                case "RepairOtherVehicles" -> {
                    if (entry.getValue() instanceof Map<?, ?> map) {
                        Map<String, Number> repairMap = (HashMap<String, Number>) map;

                        if (repairMap.containsKey("range"))
                            info.repairOtherVehiclesRange = getClamped(1_000.0F, repairMap.get("range"));

                        if (repairMap.containsKey("value"))
                            info.repairOtherVehiclesValue = getClamped(10_000_000, repairMap.get("value"));
                    }
                }
                //TODO
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
                case "NameOnModernAARadar" -> {
                    if (entry.getValue() instanceof String data)
                        info.nameOnModernAARadar = data;
                }
                case "NameOnEarlyAARadar" -> {
                    if (entry.getValue() instanceof String data)
                        info.nameOnEarlyAARadar = data;
                }
                case "NameOnModernASRadar" -> {
                    if (entry.getValue() instanceof String data)
                        info.nameOnModernASRadar = data;
                }
                case "NameOnEarlyASRadar" -> {
                    if (entry.getValue() instanceof String data)
                        info.nameOnEarlyASRadar = data;
                }
                case "ExplosionSizeByCrash" -> {
                    info.explosionSizeByCrash = info.toInt(data, 0, 100);
                }
                case "ThrottleDownFactor" -> {
                    if (entry.getValue() instanceof String data)
                        info.throttleDownFactor = info.toFloat(data, 0, 10);
                }


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
