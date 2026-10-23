import { Outlet } from "react-router-dom";

export const AppLayout = () => {
    return (
        <div>
            <div>

                HIER HEADER
            </div>

            <div>
                <Outlet/>
            </div>
        </div>
    );
};