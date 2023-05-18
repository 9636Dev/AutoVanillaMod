package org._9636dev.autosmithingtablerewrite.common.capability;

import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unused")
public interface ItemInputFilter {
    boolean isValid(AutoItemHandler itemHandler, ItemStack itemStack);

    ItemInputFilter ALLOW_ALL = (h,i) -> true;

    class SlotFilter implements ItemInputFilter {

        boolean reversed;
        protected final List<Integer> slots;

        /**
         * Creates a filter based on slot
         * @param slots valid/invalid slots
         * @param reversed if reversed, slots specified will be invalid, if not, only slots specified are valid
         */
        public SlotFilter(List<Integer> slots, boolean reversed) {
            this.reversed = reversed;
            this.slots = slots;

            sortSlots();
        }

        public void addSlot(int slot) {
            this.slots.add(slot);
            sortSlots();
        }

        public boolean removeSlot(int slot) {
            // If an item is removed, no sorting is needed, as ordering won't be affected
            return this.slots.remove(Integer.valueOf(slot));
        }

        protected final void sortSlots() {
            this.slots.sort(Comparator.naturalOrder());
        }

        @Override
        public boolean isValid(AutoItemHandler itemHandler, ItemStack itemStack) {
            // If successfully inserted, the slot is free
            for (int slot : slots) {
                boolean val = itemHandler.insertItemBypassFilter(slot, itemStack.copy(), true).equals(itemStack);
                if ((val && !reversed) || (reversed && !val)) return true;
            }
            return false;
        }
    }

    class ItemStackFilter implements ItemInputFilter {
        protected final List<ItemStack> itemStacks;

        public ItemStackFilter(List<ItemStack> validItems) {
            this.itemStacks = validItems;
        }

        public void addItem(ItemStack itemStack) {
            itemStacks.add(itemStack);
        }

        public boolean removeItem(ItemStack itemStack) {
            return itemStacks.remove(itemStack);
        }


        @Override
        public boolean isValid(AutoItemHandler itemHandler, ItemStack itemStack) {
            return itemStacks.contains(itemStack);
        }
    }
}
