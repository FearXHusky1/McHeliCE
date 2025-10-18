package com.norwood.mcheli.helper.info.parsers.yaml;

import com.norwood.mcheli.weapon.MCH_SightType;
import com.norwood.mcheli.weapon.MCH_WeaponInfo;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.norwood.mcheli.helper.info.parsers.yaml.YamlParser.getClamped;
import static com.norwood.mcheli.helper.info.parsers.yaml.YamlParser.logUnkownEntry;

@NoArgsConstructor
public class WeaponParser {

    public MCH_WeaponInfo parse(MCH_WeaponInfo info, Map<String, Object> root) {
        for (Map.Entry<String, Object> entry : root.entrySet()) {
            switch (entry.getKey()) {
                case "DisplayName" -> info.displayName = ((String) entry.getValue()).trim();
                case "Group" -> info.group = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "Power" -> info.power = ((Number) entry.getValue()).intValue();
                case "Sound" -> info.soundFileName = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "Acceleration" -> info.acceleration = getClamped(0.0F, 100.0F, (Number) entry.getValue());
                case "AccelerationInWater" ->
                        info.accelerationInWater = getClamped(0.0F, 100.0F, (Number) entry.getValue());
                case "Gravity" -> info.gravity = getClamped(-50.0F, 50.0F, (Number) entry.getValue());
                case "GravityInWater" -> info.gravityInWater = getClamped(-50.0F, 50.0F, (Number) entry.getValue());
                case "VelocityInWater" -> info.velocityInWater = ((Number) entry.getValue()).floatValue();
                case "Explosion" -> info.explosion = ((Number) entry.getValue()).intValue();
                case "ExplosionBlock" -> info.explosionBlock = ((Number) entry.getValue()).intValue();
                case "ExplosionInWater" -> info.explosionInWater = ((Number) entry.getValue()).intValue();
                case "ExplosionAltitude" -> info.explosionAltitude = ((Number) entry.getValue()).intValue();
                case "TimeFuse" -> info.timeFuse = ((Number) entry.getValue()).intValue();
                case "DelayFuse" -> info.delayFuse = ((Number) entry.getValue()).intValue();
                case "Bound" -> info.bound = getClamped(0.0F, 100000.0F, (Number) entry.getValue());
                case "Flaming" -> info.flaming = ((Boolean) entry.getValue());
                case "DisplayMortarDistance" -> info.displayMortarDistance = ((Boolean) entry.getValue());
                case "FixCameraPitch" -> info.fixCameraPitch = ((Boolean) entry.getValue());
                case "CameraRotationSpeedPitch" ->
                        info.cameraRotationSpeedPitch = getClamped(0.0F, 100.0F, (Number) entry.getValue());
                case "Sight" -> {
                    String value = ((String) entry.getValue()).toLowerCase(Locale.ROOT);
                    if (value.equals("movesight")) info.sight = MCH_SightType.ROCKET;
                    else if (value.equals("missilesight")) info.sight = MCH_SightType.LOCK;
                }
                case "Zoom" -> {
                    Object val = entry.getValue();
                    if (val instanceof List<?> list && !list.isEmpty()) {
                        info.zoom = new float[list.size()];
                        for (int i = 0; i < list.size(); i++) {
                            info.zoom[i] = getClamped(0.1F, 10.0F, (Number) list.get(i));
                        }
                    }
                }
                case "Delay" -> info.delay = ((Number) entry.getValue()).intValue();
                case "ExplosionType" -> info.explosionType = (String) entry.getValue();
                case "NukeYield" -> info.nukeYield = getClamped(100000, (Number) entry.getValue());
                case "ChemYield" -> info.chemYield = getClamped(100000, (Number) entry.getValue());
                case "EffectYield" -> info.effectYield = getClamped(100000, (Number) entry.getValue());
                case "NukeEffectOnly" -> info.nukeEffectOnly = ((Boolean) entry.getValue());
                case "MaxDegreeOfMissile" -> info.maxDegreeOfMissile = getClamped(100000, (Number) entry.getValue());
                case "TickEndHoming" -> info.tickEndHoming = getClamped(-1, 100000, (Number) entry.getValue());
                case "FlakParticlesCrack" -> info.flakParticlesCrack = getClamped(300, (Number) entry.getValue());
                case "ParticlesFlak" -> info.numParticlesFlak = getClamped(100, (Number) entry.getValue());
                case "FlakParticlesDiff" -> info.flakParticlesDiff = ((Number) entry.getValue()).floatValue();
                case "IsRadarMissile" -> info.isRadarMissile = ((Boolean) entry.getValue());
                case "IsHeatSeekerMissile" -> info.isHeatSeekerMissile = ((Boolean) entry.getValue());
                case "MaxLockOnRange" -> info.maxLockOnRange = getClamped(2000, (Number) entry.getValue());
                case "MaxLockOnAngle" -> info.maxLockOnAngle = getClamped(200, (Number) entry.getValue());
                case "PDHDNMaxDegree" -> info.pdHDNMaxDegree = getClamped(-1.0F, 90.0F, (Number) entry.getValue());
                case "PDHDNMaxDegreeLockOutCount" ->
                        info.pdHDNMaxDegreeLockOutCount = getClamped(200, (Number) entry.getValue());
                case "AntiFlareCount" -> info.antiFlareCount = getClamped(-1, 200, (Number) entry.getValue());
                case "LockMinHeight" -> info.lockMinHeight = getClamped(-1, 100, (Number) entry.getValue());
                case "PassiveRadar" -> info.passiveRadar = ((Boolean) entry.getValue());
                case "PassiveRadarLockOutCount" ->
                        info.passiveRadarLockOutCount = getClamped(200, (Number) entry.getValue());
                case "LaserGuidance" -> info.laserGuidance = ((Boolean) entry.getValue());
                case "HasLaserGuidancePod" -> info.hasLaserGuidancePod = ((Boolean) entry.getValue());
                case "ActiveRadar" -> info.activeRadar = ((Boolean) entry.getValue());
                case "EnableOffAxis" -> info.enableOffAxis = ((Boolean) entry.getValue());
                case "TurningFactor" -> info.turningFactor = ((Number) entry.getValue()).doubleValue();
                case "EnableChunkLoader" -> info.enableChunkLoader = (Boolean) entry.getValue();
                case "ScanInterval" -> info.scanInterval = ((Number) entry.getValue()).intValue();
                case "WeaponSwitchCount" -> info.weaponSwitchCount = ((Number) entry.getValue()).intValue();
                case "WeaponSwitchSound" ->
                        info.weaponSwitchSound = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "RecoilPitch" -> info.recoilPitch = ((Number) entry.getValue()).floatValue();
                case "RecoilYaw" -> info.recoilYaw = ((Number) entry.getValue()).floatValue();
                case "RecoilPitchRange" -> info.recoilPitchRange = ((Number) entry.getValue()).floatValue();
                case "RecoilYawRange" -> info.recoilYawRange = ((Number) entry.getValue()).floatValue();
                case "RecoilRecoverFactor" -> info.recoilRecoverFactor = ((Number) entry.getValue()).floatValue();
                case "SpeedFactor" -> info.speedFactor = ((Number) entry.getValue()).floatValue();
                case "SpeedFactorStartTick" -> info.speedFactorStartTick = ((Number) entry.getValue()).intValue();
                case "SpeedFactorEndTick" -> info.speedFactorEndTick = ((Number) entry.getValue()).intValue();
                case "SpeedDependsAircraft" -> info.speedDependsAircraft = (Boolean) entry.getValue();
                case "CanLockMissile" -> info.canLockMissile = (Boolean) entry.getValue();
                case "EnableBVR" -> info.enableBVR = (Boolean) entry.getValue();
                case "MinRangeBVR" -> info.minRangeBVR = ((Number) entry.getValue()).intValue();
                case "PredictTargetPos" -> info.predictTargetPos = (Boolean) entry.getValue();
                case "HitSound" -> info.hitSound = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "HitSoundIron" -> info.hitSoundIron = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "HitSoundRange" -> info.hitSoundRange = ((Number) entry.getValue()).intValue();
                case "NumLockedChaffMax" -> info.numLockedChaffMax = ((Number) entry.getValue()).intValue();
                case "ExplosionDamageVsLiving" ->
                        info.explosionDamageVsLiving = ((Number) entry.getValue()).floatValue();
                case "ExplosionDamageVsPlayer" ->
                        info.explosionDamageVsPlayer = ((Number) entry.getValue()).floatValue();
                case "ExplosionDamageVsPlane" -> info.explosionDamageVsPlane = ((Number) entry.getValue()).floatValue();
                case "ExplosionDamageVsVehicle" ->
                        info.explosionDamageVsVehicle = ((Number) entry.getValue()).floatValue();
                case "ExplosionDamageVsTank" -> info.explosionDamageVsTank = ((Number) entry.getValue()).floatValue();
                case "ExplosionDamageVsHeli" -> info.explosionDamageVsHeli = ((Number) entry.getValue()).floatValue();
                case "DisableDestroyBlock" -> info.disableDestroyBlock = (Boolean) entry.getValue();
                case "RailgunSound" -> info.railgunSound = ((String) entry.getValue()).toLowerCase(Locale.ROOT).trim();
                case "CanBeIntercepted" -> info.canBeIntercepted = (Boolean) entry.getValue();
                case "CanAirburst" -> info.canAirburst = (Boolean) entry.getValue();
                case "ExplosionAirburst" -> info.explosionAirburst = getClamped(50, (Number) entry.getValue());
                case "CrossType" -> info.crossType = ((Number) entry.getValue()).intValue();
                case "EnableMortarRadar" -> info.hasMortarRadar = (Boolean) entry.getValue();
                case "MortarRadarMaxDist" -> info.mortarRadarMaxDist = ((Number) entry.getValue()).doubleValue();
                case "MarkerRocketSpawnNum" -> info.markerRocketSpawnNum = ((Number) entry.getValue()).intValue();
                case "MarkerRocketSpawnDiff" -> info.markerRocketSpawnDiff = ((Number) entry.getValue()).intValue();
                case "MarkerRocketSpawnHeight" -> info.markerRocketSpawnHeight = ((Number) entry.getValue()).intValue();
                case "MarkerRocketSpawnSpeed" -> info.markerRocketSpawnSpeed = ((Number) entry.getValue()).intValue();
                case "ReloadTime" -> info.reloadTime = getClamped(3, 1000, (Number) entry.getValue());
                case "Round" -> info.round = getClamped(1, 30000, (Number) entry.getValue());
                case "MaxAmmo" -> info.maxAmmo = getClamped(30000, (Number) entry.getValue());
                case "SuppliedNum" -> info.suppliedNum = getClamped(1, 30000, (Number) entry.getValue());


                default -> logUnkownEntry(entry, "Weapon");


            }
        }
        return info;
    }


}
