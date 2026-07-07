import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { convertFileToBase64, triggerBinaryDownload } from './fileUtils';

describe('fileUtils', () => {

    describe('convertFileToBase64', () => {
        it('should convert valid file into base64 string', async () => {
            const fileContent = 'Hallo Welt';
            const fakeFile = new File([fileContent], 'test.txt', { type: 'text/plain' });

            const result = await convertFileToBase64(fakeFile);

            expect(result).toContain('data:text/plain;base64,');
            expect(result).toContain('SGFsbG8gV2VsdA==');
        });

        it('should reject the promise if the file reading fails', async () => {
            const validFile = new File(['test'], 'error.txt', { type: 'text/plain' });

            const fileReaderSpy = vi.spyOn(FileReader.prototype, 'readAsDataURL')
                .mockImplementation(function (this: FileReader) {
                    if (this.onerror) {
                        this.onerror(new ProgressEvent('error') as any);
                    }
                });

            await expect(convertFileToBase64(validFile))
                .rejects
                .toThrow('Fehler beim Lesen der Datei.');

            fileReaderSpy.mockRestore();
        });
    });

    describe('triggerBinaryDownload', () => {
        const originalCreateObjectURL = window.URL.createObjectURL;
        const originalRevokeObjectURL = window.URL.revokeObjectURL;

        beforeEach(() => {
            // JSDOM polyfill, as these methods missing in node
            window.URL.createObjectURL = vi.fn().mockReturnValue('blob:http://localhost/mock-uuid');
            window.URL.revokeObjectURL = vi.fn();
        });

        afterEach(() => {
            window.URL.createObjectURL = originalCreateObjectURL;
            window.URL.revokeObjectURL = originalRevokeObjectURL;
        });

        it('should create a virtual link, click it and clean up the DOM', () => {
            const fakeBlob = new Blob(['dummy content'], { type: 'application/pdf' });
            const testFileName = 'document.pdf';

            const clickSpy = vi.spyOn(HTMLAnchorElement.prototype, 'click');

            expect(document.body.querySelector('a')).toBeNull();

            triggerBinaryDownload(fakeBlob, testFileName);

            expect(window.URL.createObjectURL).toHaveBeenCalledWith(fakeBlob);

            expect(clickSpy).toHaveBeenCalledTimes(1);

            expect(document.body.querySelector('a')).toBeNull();

            expect(window.URL.revokeObjectURL).toHaveBeenCalledWith('blob:http://localhost/mock-uuid');

            // Spion aufräumen
            clickSpy.mockRestore();
        });
    });
});