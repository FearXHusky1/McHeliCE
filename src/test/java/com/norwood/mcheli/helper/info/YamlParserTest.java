package com.norwood.mcheli.helper.info;

import com.norwood.mcheli.aircraft.MCH_AircraftInfo;
import com.norwood.mcheli.aircraft.MCH_SeatInfo;
import com.norwood.mcheli.aircraft.MCH_SeatRackInfo;
import com.norwood.mcheli.helicopter.MCH_HeliInfo;
import com.norwood.mcheli.helper.MCH_Logger;
import com.norwood.mcheli.helper.info.parsers.yaml.ComponentParser;
import com.norwood.mcheli.helper.info.parsers.yaml.YamlParser;
import com.norwood.mcheli.hud.MCH_Hud;
import com.norwood.mcheli.plane.MCH_PlaneInfo;
import com.norwood.mcheli.vehicle.MCH_VehicleInfo;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import com.norwood.mcheli.helper.addon.AddonResourceLocation;
import com.norwood.mcheli.wrapper.W_Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>Test class for {@link YamlParser}.</p>
 * Note that classloading <strong>must not</strong> cascade into any class under package {@link net.minecraft.init}, be careful with classes' clinit
 */
class YamlParserTest {
    @BeforeAll
    static void setupLogger(){
        MCH_Logger.setLogger(LogManager.getLogger("TestLogger"));
    }

    @TempDir
    Path tempDir;

}
