import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

export default function LoginPage() {
    return (
        <div className="max-w-md mx-auto">
            <h1 className="text-3xl font-bold mb-6">Admin login</h1>
            <div className="space-y-4">
                <div>
                    <Label htmlFor="username">Username</Label>
                    <Input id="username" placeholder="admin" />
                </div>
                <div>
                    <Label htmlFor="password">Password</Label>
                    <Input id="password" type="password" />
                </div>
                <Button className="w-full">Sign in</Button>
            </div>
        </div>
    );
}