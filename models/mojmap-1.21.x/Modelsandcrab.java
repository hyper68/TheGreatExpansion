// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class Modelsandcrab<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in
	// the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "sandcrab"), "main");
	private final ModelPart bone2;
	private final ModelPart bone;
	private final ModelPart Body;

	public Modelsandcrab(ModelPart root) {
		this.bone2 = root.getChild("bone2");
		this.bone = root.getChild("bone");
		this.Body = root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create(),
				PartPose.offset(-3.05F, 23.5F, -2.75F));

		PartDefinition cube_r1 = bone2.addOrReplaceChild("cube_r1",
				CubeListBuilder.create().texOffs(8, 7)
						.addBox(1.25F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)).texOffs(4, 7)
						.addBox(2.65F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)).texOffs(4, 5)
						.addBox(4.05F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create(),
				PartPose.offset(-0.25F, 23.0F, 0.5F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2",
				CubeListBuilder.create().texOffs(0, 5)
						.addBox(1.25F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)).texOffs(0, 7)
						.addBox(-0.15F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)).texOffs(8, 5)
						.addBox(-1.55F, -0.75F, 0.0F, 0.75F, 0.75F, 1.25F, new CubeDeformation(0.0F)),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3927F, 0.0F, 0.0F));

		PartDefinition Body = partdefinition.addOrReplaceChild("Body",
				CubeListBuilder.create().texOffs(0, 0)
						.addBox(-2.0F, -2.0F, -2.0F, 4.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).texOffs(4, 9)
						.addBox(1.75F, -2.5F, -1.75F, 0.5F, 0.75F, 0.5F, new CubeDeformation(0.0F)).texOffs(0, 9)
						.addBox(1.75F, -2.5F, 0.25F, 0.5F, 0.75F, 0.5F, new CubeDeformation(0.0F)),
				PartPose.offset(0.0F, 23.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
			float red, float green, float blue, float alpha) {
		bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch) {
		this.bone2.yRot = Mth.cos(limbSwing * 1.0F) * 1.0F * limbSwingAmount;
		this.bone.yRot = Mth.cos(limbSwing * 1.0F) * -1.0F * limbSwingAmount;
	}
}