package ru.practicum.shareit.item_test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemResult;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemResultJsonTests {
    @Autowired
    JacksonTester<ItemResult> json;

    @Test
    void testItemResult() throws Exception {

        ItemResult.LastBooking lastBooking = new ItemResult.LastBooking(1L, 1L);
        ItemResult.NextBooking nextBooking = new ItemResult.NextBooking(2L, 1L);

        ItemResult item = new ItemResult(
                1L,
                "item",
                "desc",
                true,
                nextBooking,
                lastBooking,
                new ArrayList<>(),
                null
        );

        JsonContent<ItemResult> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("desc");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathValue("$.lastBooking.id").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.nextBooking.id").isEqualTo(2);

        lastBooking.setId(3L);
        lastBooking.setBookerId(2L);
        nextBooking.setId(4L);
        nextBooking.setBookerId(2L);

        item.setLastBooking(lastBooking);
        item.setNextBooking(nextBooking);

        JsonContent<ItemResult> result2 = json.write(item);

        assertThat(result2).extractingJsonPathValue("$.lastBooking.id").isEqualTo(3);
        assertThat(result2).extractingJsonPathValue("$.nextBooking.id").isEqualTo(4);
    }
}
