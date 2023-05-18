package org._9636dev.autosmithingtablerewrite.common.capability;

import java.util.List;

@SuppressWarnings("unused")
public interface ItemOutputFilter {
    boolean isValid(AutoItemHandler itemHandler, int slot);

    ItemOutputFilter ALLOW_ALL = (i, s) -> true;

    class SlotFilter implements ItemOutputFilter{

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
        }

        public void addSlot(int slot) {
            this.slots.add(slot);
        }

        public boolean removeSlot(int slot) {
            // If an item is removed, no sorting is needed, as ordering won't be affected
            return this.slots.remove(Integer.valueOf(slot));
        }

        @Override
        public boolean isValid(AutoItemHandler itemHandler, int slot) {
            if (!reversed) return this.slots.contains(slot);
            return !this.slots.contains(slot);
        }
    }
}
