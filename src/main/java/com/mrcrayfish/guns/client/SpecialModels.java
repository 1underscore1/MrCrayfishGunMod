package com.mrcrayfish.guns.client;

import com.mrcrayfish.guns.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum SpecialModels
{
    ASSAULT_RIFLE("gun/assault_rifle"),
    BAZOOKA("gun/bazooka"),
    HEAVY_RIFLE("gun/heavy_rifle"),
    MACHINE_PISTOL("gun/machine_pistol"),
    PISTOL("gun/pistol"),
    RIFLE("gun/rifle"),
    SHOTGUN("gun/shotgun"),
    FLAME("flame"),
    MINI_GUN_BASE("mini_gun_base"),
    MINI_GUN_BARRELS("mini_gun_barrels"),
    GRENADE_LAUNCHER_BASE("grenade_launcher_base"),
    GRENADE_LAUNCHER_CYLINDER("grenade_launcher_cylinder"),
    SUPER_SHOTGUN("gun/super_shotgun"),
    LASER_PISTOL("gun/laser_pistol"),
    LASER_RING("laser_ring"),
    NAIL_UZI("gun/nail_uzi"),
    NAIL_UZI_BASE("nail_uzi_base"),
    NAIL_UZI_NAIL("nail_uzi_nail"),
    NAIL_SHOTGUN("gun/nail_shotgun"),
    NAIL_SHOTGUN_BASE("nail_shotgun_base"),
    NAIL_SHOTGUN_BARRELS("nail_shotgun_barrels"),
    GATLING_ROCKET_SHOTGUN("gun/gatling_rocket_shotgun");

    /**
     * The location of an item model in the [MOD_ID]/models/special/[NAME] folder
     */
    private ResourceLocation modelLocation;

    /**
     * Determines if the model should be loaded as a special model.
     */
    private boolean specialModel;

    /**
     * Cached model
     */
    @OnlyIn(Dist.CLIENT)
    private BakedModel cachedModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    SpecialModels(String modelName)
    {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName), true);
    }

    /**
     * Sets the model's location
     *
     * @param resource name of the model file
     * @param specialModel if the model is a special model
     */
    SpecialModels(ResourceLocation resource, boolean specialModel)
    {
        this.modelLocation = resource;
        this.specialModel = specialModel;
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @OnlyIn(Dist.CLIENT)
    public BakedModel getModel()
    {
        if(this.cachedModel == null)
        {
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(this.modelLocation);
            if(model == Minecraft.getInstance().getModelManager().getMissingModel())
            {
                return model;
            }
            this.cachedModel = model;
        }
        return this.cachedModel;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void register(ModelRegistryEvent event)
    {
        for(SpecialModels model : values())
        {
            if(model.specialModel)
            {
                ForgeModelBakery.addSpecialModel(model.modelLocation);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearCache()
    {
        for(SpecialModels model : values())
        {
            if(model.specialModel)
            {
                model.cachedModel = null;
            }
        }
    }
}
