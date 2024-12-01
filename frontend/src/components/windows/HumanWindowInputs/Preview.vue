<template>
  <div >
    <div class="grid grid-cols-2 gap-4">
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Car Name</h3>
          <p class="text-white">{{ data.car.name }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Coordinates</h3>
          <p class="text-white">x: {{ data.coordinates.x }}, y: {{ data.coordinates.y }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Name</h3>
          <p class="text-white">{{ data.human.name }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Soundtrack Name</h3>
          <p class="text-white">{{ data.human.soundtrackName }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Impact Speed</h3>
          <p class="text-white">{{ data.human.impactSpeed }}</p>
        </div>
      </div>
      <div class="flex flex-col space-y-2">
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Minutes of Waiting</h3>
          <p class="text-white">{{ data.human.minutesOfWaiting }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Weapon Type</h3>
          <p class="text-white">{{ data.human.weaponType }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Mood</h3>
          <p class="text-white">{{ data.human.mood }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Real Hero</h3>
          <p class="text-white">{{ data.human.realHero ? 'Yes' : 'No' }}</p>
        </div>
        <div class="flex justify-between items-center border-b border-gray-600 py-2">
          <h3 class="text-lg font-semibold text-white">Has Toothpick</h3>
          <p class="text-white">{{ data.human.hasToothpick ? 'Yes' : 'No' }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  data: Object
});
</script>