interface LoadingSpinnerProps {
    label?: string
}

export const LoadingSpinner = ({label = "Loading user data" } : LoadingSpinnerProps) => {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen gap-3">
            <div className="w-8 h-8 border-4 border-blue-500 border-t-transparent rounded-full animate-spin" />
            {label && <p className="text-sm text-gray-600 font-medium">{label}</p>}
        </div>
    );
};  