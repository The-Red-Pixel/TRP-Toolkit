package work.erio.toolkit.module;

import com.rabbit.gui.RabbitGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import work.erio.toolkit.gui.GuiTest;
import work.erio.toolkit.tile.TileEntityPulse;

public class ModuleTest extends AbstractModule implements IModule {

    public ModuleTest() {
        super(false);
    }

    @Override
    public String getTitle() {
        return "Test";
    }

    @Override
    public void onUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    }

    @Override
    public void onServerUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        RabbitGui.proxy.display(new GuiTest());
    }

    @Override
    public void onRender(RenderWorldLastEvent evt) {

    }
}
