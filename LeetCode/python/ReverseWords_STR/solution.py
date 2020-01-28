class Solution:
    def reverseWords(self, s: str) -> str:
        """ Reverses string word by word
        
        Args:
            s (str): The string to be reversed word by word
        
        Returns:
            str: the string reversed word by word
        """
        
        
        # Remove leading/trailing spaces and multiple spaces
        # between words
        parsed_s = " ".join(s.split())
        
        # Keep track of the index of each remaining space character
        space_list = [];
        
        for i in range(0, len(parsed_s)):
            if (parsed_s[i].isspace()):
                space_list.append(i)
        
        num_spaces = len(space_list)
        index = num_spaces - 1
        end_word = len(parsed_s)
        result = ""
        
        # Reverse string word by word
        while index > -1:
            result += parsed_s[space_list[index] + 1:end_word] + " "
            end_word = space_list[index]
            index -= 1
        
        result += parsed_s[0:end_word]
        return result
        