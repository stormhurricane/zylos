import { describe, it, expect } from 'vitest';
import { convertFileToBase64 } from './fileUtils';

describe('fileUtils - convertFileToBase64', () => {
    
    it('should convert valid file into base64 string', async () => {
        // real text as file content
        const fileContent = 'Hallo Welt';
        const fakeFile = new File([fileContent], 'test.txt', { type: 'text/plain' });

        // call real function and wait for promise
        const result = await convertFileToBase64(fakeFile);

        // check base64-data-url format for text files starts with "data:text/plain;base64,"
        expect(result).toContain('data:text/plain;base64,');
        
        // check that the base64 part contains the expected encoded string
        expect(result).toContain('SGFsbG8gV2VsdA==');
    });

    it('should reject the promise if the file reading fails', async () => {
        // To simulate a file reading error, we can create a mock File object that will cause the FileReader to fail.
        const brokenFile = { size: 1024, type: 'image/png' } as unknown as File;

        await expect(convertFileToBase64(brokenFile)).rejects.toThrow();
    });
});