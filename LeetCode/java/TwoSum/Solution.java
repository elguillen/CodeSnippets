class Solution {
    
    /**
     * Obtains the indices of the the two numbers that add up to
     * target. Assumes each input has one solution and the same
     * element can not be used twice.
     *
     * @param nums   an array of integers
     * @param target The target value to look for.
     * @return       An array of integers corresponding to the two
     *               indices of the numbers that add up to target.
    **/
    
    public int[] twoSum(int[] nums, int target) {
        
        // Used to store values and their corresponding index
        HashMap<Integer, Integer> twoSumMap = new HashMap<>();
        
        int[] answer = new int[2];
        
        twoSumMap.put(nums[0], 0);
        
        // Given that we want to find nums[x] + nums[y] = target
        // let result be nums[y] such that result = target - nums[x]
        int result;
        
        for (int i = 1; i < nums.length; i++) {
            result = target - nums[i];
            
            // if result is in the hashmap then result
            // and nums[i] are the pair we are looking for
            // otherwise inerst nums[i] into the hashmap
            if (twoSumMap.containsKey(result)) {
                answer[1] = i;
                answer[0] = twoSumMap.get(result);
                break;
            } else {
                twoSumMap.put(nums[i], i);
            }
        }
        
        return answer;
    }
}